package tests.authRouteTests

import com.auth0.jwt.JWT
import com.example.features.auth.domain.AuthConstants.EMAIL_PASSWORD_INCORRECT
import data.TestConstants.TEST_CREATED_OBJECT
import data.TestConstants.TEST_USER_EMAIL
import data.TestConstants.TEST_USER_PASSWORD
import fakeDataSource.TestRepository
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import tests.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class Login : KoinTest {

    @Test
    fun `should return ok if user is not logged`() {
        runServer {
            handleGetRequest("/auth/login") {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun `should redirect if user is already logged`() {
        runServer {
            runWithLoggedUser {
                handleGetRequest("/auth/login") {
                    assertEquals(HttpStatusCode.Found, response.status())
                }
            }
        }
    }

    @Test
    fun `should return error for invalid credentials`() {
        runServer {
            handlePostRequest(
                "/auth/login",
                listOf(
                    "email" to "INVALID_EMAIL",
                    "password" to "INVALID_PASSWORD"
                )
            ) {
                assertEquals(EMAIL_PASSWORD_INCORRECT, response.content)
            }
        }
    }

    @Test
    fun `should return token for Jwt login`() {
        runServer {
            handlePostRequest(
                "/auth/login",
                listOf(
                    "Email" to TEST_USER_EMAIL,
                    "Password" to TEST_USER_PASSWORD
                )
            ) {
                assertNotEquals(EMAIL_PASSWORD_INCORRECT, response.content)

                val token = response.content!!
                println(token)
                val decodedJWT = JWT.decode(token)
                assertEquals("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9", decodedJWT.header)
                assertEquals(TEST_USER_EMAIL, decodedJWT.getClaim("email").asString())
                //assertEquals(TEST_USER_USERNAME, decodedJWT.getClaim("username").asString())
            }
        }
    }

    @Test
    fun `should sync cookie objects to account after login`() {
        runServer {
            cookiesSession {
                `create object before user login`()
                userLogin()
                runBlocking {
                    val testRepository by inject<TestRepository>()
                    val obj = testRepository.getUser(TEST_USER_EMAIL)
                        .objects
                        .find { it.id == TEST_CREATED_OBJECT }
                    assertNotNull(obj)
                }
            }
        }
    }
}