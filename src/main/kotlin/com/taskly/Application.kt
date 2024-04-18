package com.taskly

import com.taskly.Repository.DatabaseFactory
import com.taskly.Repository.repo
import com.taskly.authentication.JWTService
import com.taskly.authentication.hash
import com.taskly.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 5050, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()
    val db = repo()
    val jwtService = JWTService()
    val hashFunction = { s: String -> hash(s) }

    configureSecurity()
    configureSerialization()
    configureRouting()
}
