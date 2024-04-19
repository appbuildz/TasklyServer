package com.taskly.plugins

import com.taskly.Repository.DatabaseFactory
import com.taskly.Repository.repo
import com.taskly.authentication.JWTService
import com.taskly.authentication.hash
import com.taskly.data.model.User
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {


    routing {
        get("/") {
            call.respondText("Hello Dhruv!")
        }

        get("/token") {
            val email = call.request.queryParameters["email"]!!
            val password = call.request.queryParameters["password"]!!
            val username = call.request.queryParameters["username"]!!

            val user = User(email, hash(password), username)
            call.respond(JWTService().generateToken(user))
        }

    }
}
