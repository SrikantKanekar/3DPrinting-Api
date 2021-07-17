package com.example.features.auth.presentation

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.config.JWTConfig
import com.example.features.auth.data.AuthRepository
import com.example.features.auth.domain.AuthConstants.EMAIL_PASSWORD_INCORRECT
import com.example.features.auth.domain.LoginRequest
import com.example.features.auth.domain.UserPrincipal
import com.example.features.objects.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.getLoginRoute() {
    get("/auth/login") {
        val principal = call.sessions.get<UserPrincipal>()
        if (principal != null) {
            call.respondRedirect("/account")
        } else {
            val returnUrl = call.parameters["returnUrl"] ?: "/"
            call.respond(FreeMarkerContent("auth_login.ftl", mapOf("returnUrl" to returnUrl)))
        }
    }
}

// after jwt
fun Route.postLoginRoute(authRepository: AuthRepository, jwt: JWTConfig) {
    post("/auth/login") {
        val (email, password) = call.receive<LoginRequest>()

        val isPasswordCorrect = authRepository.login(email, password)
        if (isPasswordCorrect) {
            call.sessions.set(UserPrincipal(email))
            val cookie = call.sessions.get<ObjectsCookie>()
            authRepository.syncCookieObjects(email, cookie)
            call.sessions.clear<ObjectsCookie>()

            val token = JWT.create()
                .withIssuer(jwt.issuer)
                .withAudience(jwt.audience)
                .withClaim("username", "username")
                .withClaim("email", email)
                .sign(Algorithm.HMAC256(jwt.secret))

            call.respond(token)
        } else {
            call.respondText(EMAIL_PASSWORD_INCORRECT)
        }
    }
}