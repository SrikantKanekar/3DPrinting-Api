package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.domain.*
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.auth.domain.UserPrincipal
import com.example.features.userObject.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

/**
 * This page will show the details about a object depending upon the ObjectStatus of the object.
 * 1) [NONE]/[CART] - user can edit object properties.
 * 2) [TRACKING]
 * 3) [COMPLETED]
 */
fun Route.getObjectRoute(objectRepository: ObjectRepository) {
    get("/object/{id}") {

        val id = call.parameters["id"]!!
        val principal = call.sessions.get<UserPrincipal>()

        val obj = when (principal) {
            null -> call.sessions.get<ObjectsCookie>()?.objects?.find { it.id == id }
            else -> objectRepository.getUserObject(principal.email, id)
        }
        when (obj) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(
                FreeMarkerContent(
                    "object.ftl", mapOf(
                        "object" to obj,
                        "user" to (principal?.email ?: "")
                    )
                )
            )
        }
    }
}

fun Route.addToCart(objectRepository: ObjectRepository) {
    post("/object/add-to-cart") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()
        when (principal) {
            null -> call.respondText("/auth/login?returnUrl=/object/$id")
            else -> {
                val result = objectRepository.addToCart(principal.email, id)
                call.respond(result.toString())
            }
        }
    }
}

fun Route.removeFromCart(objectRepository: ObjectRepository) {
    post("/object/remove-from-cart") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()!!
        val result = objectRepository.removeFromCart(principal.email, id)
        call.respond(result)
    }
}

fun Route.updateQuantity(objectRepository: ObjectRepository) {
    post("/object/quantity") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val quantity = params["quantity"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()
        var result = false
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                if (quantity > 0) {
                    cookie.objects
                        .filter { it.status == NONE }
                        .find { it.id == id }
                        ?.let {
                            it.quantity = quantity
                            result = true
                        }
                    call.sessions.set(cookie)
                }
            }
            else -> {
                result = objectRepository.updateQuantity(principal.email, id, quantity)
            }
        }
        call.respond(result)
    }
}

fun Route.updateBasicSettings(objectRepository: ObjectRepository) {
    post("/object/update/basic-settings") {

        val params = call.receiveParameters()

        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val layerHeight = params["layer_height"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val wallThickness = params["wall_thickness"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val infillDensity = params["infill_density"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val infillPattern = params["infill_pattern"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val generateSupport = params["generate_support"]
        val supportStructure = params["support_structure"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportPlacement = params["support_placement"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportOverhangAngle = params["support_overhang_angle"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportPattern = params["support_pattern"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val supportDensity = params["support_density"]?.toFloat() ?: return@post call.respond(HttpStatusCode.BadRequest)

        val basicSettings = BasicSettings(
            layerHeight = layerHeight,
            wallThickness = wallThickness,
            infillDensity = infillDensity,
            infillPattern = InfillPattern.valueOf(infillPattern),
            generateSupport = generateSupport == "on",
            supportStructure = SupportStructure.valueOf(supportStructure),
            supportPlacement = SupportPlacement.valueOf(supportPlacement),
            supportOverhangAngle = supportOverhangAngle,
            supportPattern = SupportPattern.valueOf(supportPattern),
            supportDensity = supportDensity
        )
        val principal = call.sessions.get<UserPrincipal>()
        var updated = false
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == NONE || it.status == CART }
                    .find { it.id == id }
                    ?.let {
                        it.basicSettings = basicSettings
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated =
                objectRepository.updateBasicSettings(principal.email, id, basicSettings)
        }
        call.respond(updated)
    }
}

fun Route.updateAdvancedSettings(objectRepository: ObjectRepository) {
    post("/object/update/advanced-settings") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val weight = params["weight"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)

        val advancedSettings = AdvancedSettings(weight = weight)

        val principal = call.sessions.get<UserPrincipal>()

        var updated = false
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == NONE || it.status == CART }
                    .find { it.id == id }
                    ?.let {
                        it.advancedSettings = advancedSettings
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated =
                objectRepository.updateAdvancedSettings(principal.email, id, advancedSettings)
        }
        call.respond(updated)
    }
}
