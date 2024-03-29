package tests

import com.example.features.auth.domain.LoginRequest
import com.example.module
import com.example.util.constants.Auth.EMAIL_PASSWORD_INCORRECT
import data.TestConstants.TEST_ADMIN_TOKEN
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import data.TestConstants.TEST_USER_TOKEN
import di.testModules
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.assertNotEquals

fun MapApplicationConfig.configForTesting() {
    put("ktor.jwt.issuer", "3D printing api")
    put("ktor.jwt.audience", "printer audience")
    put("ktor.jwt.realm", "3D printing api")
}

fun runServer(test: TestApplicationEngine.() -> Unit) {
    withTestApplication({
        (environment.config as MapApplicationConfig).apply {
            configForTesting()
        }
        module(testing = true, koinModules = testModules)
    }) {
        test()
    }
}

fun TestApplicationEngine.handleGetRequest(
    uri: String,
    logged: Boolean = false,
    admin: Boolean = false,
    assert: TestApplicationCall.() -> Unit
) {
    handleRequest(HttpMethod.Get, uri) {
        if (logged) addHeader(HttpHeaders.Authorization, "Bearer $TEST_USER_TOKEN")
        if (admin) addHeader(HttpHeaders.Authorization, "Bearer $TEST_ADMIN_TOKEN")
    }.apply {
        assert()
    }
}

inline fun <reified T> TestApplicationEngine.handlePostRequest(
    uri: String,
    body: T,
    logged: Boolean = false,
    admin: Boolean = false,
    assert: TestApplicationCall.() -> Unit
) {
    handleRequest(HttpMethod.Post, uri) {
        addHeader(
            HttpHeaders.ContentType,
            ContentType.Application.Json.toString()
        )
        val jsonBody = Json.encodeToString(body)
        setBody(jsonBody)
        if (logged) addHeader(HttpHeaders.Authorization, "Bearer $TEST_USER_TOKEN")
        if (admin) addHeader(HttpHeaders.Authorization, "Bearer $TEST_ADMIN_TOKEN")
    }.apply {
        assert()
    }
}

inline fun <reified T> TestApplicationEngine.handlePutRequest(
    uri: String,
    body: T,
    logged: Boolean = false,
    admin: Boolean = false,
    assert: TestApplicationCall.() -> Unit
) {
    handleRequest(HttpMethod.Put, uri) {
        addHeader(
            HttpHeaders.ContentType,
            ContentType.Application.Json.toString()
        )
        val jsonBody = Json.encodeToString(body)
        setBody(jsonBody)
        if (logged) addHeader(HttpHeaders.Authorization, "Bearer $TEST_USER_TOKEN")
        if (admin) addHeader(HttpHeaders.Authorization, "Bearer $TEST_ADMIN_TOKEN")
    }.apply {
        assert()
    }
}

fun TestApplicationEngine.handleDeleteRequest(
    uri: String,
    logged: Boolean = false,
    admin: Boolean = false,
    assert: TestApplicationCall.() -> Unit
) {
    handleRequest(HttpMethod.Delete, uri) {
        if (logged) addHeader(HttpHeaders.Authorization, "Bearer $TEST_USER_TOKEN")
        if (admin) addHeader(HttpHeaders.Authorization, "Bearer $TEST_ADMIN_TOKEN")
    }.apply {
        assert()
    }
}

fun TestApplicationEngine.userLogin() {
    handlePostRequest(
        uri = "/auth/login",
        body = LoginRequest(TEST_USER_EMAIL, TEST_USER_PASSWORD)
    ) {
        assertNotEquals(EMAIL_PASSWORD_INCORRECT, response.content)
    }
}