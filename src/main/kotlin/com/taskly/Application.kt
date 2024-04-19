package com.taskly

import com.taskly.Repository.DatabaseFactory
import com.taskly.Repository.repo
import com.taskly.authentication.JWTService
import com.taskly.authentication.hash
import com.taskly.data.model.User
import com.taskly.plugins.*
import com.taskly.routes.UserRoutes
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*


fun main() {
    embeddedServer(Netty, port = 5050, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()
    val db = repo()
    val jwtService = JWTService()
    val hashFunction = { s: String -> hash(s) }


    routing {
        UserRoutes(db, jwtService, hashFunction)
    }

    configureSecurity()
    configureSerialization()
    configureRouting()
}
