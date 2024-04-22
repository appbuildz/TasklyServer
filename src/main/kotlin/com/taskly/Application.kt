package com.taskly

import com.taskly.Repository.DatabaseFactory
import com.taskly.Repository.Repo
import com.taskly.authentication.JWTService
import com.taskly.authentication.hash
import com.taskly.plugins.*
import com.taskly.routes.TaskRoutes
import com.taskly.routes.UserRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*


fun main() {
    embeddedServer(Netty, port = 5050, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()
    val db = Repo()
    val jwtService = JWTService()
    val hashFunction = { s: String -> hash(s) }

    install(Authentication){
    val jwtRealm = "Taskly Server"
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = jwtRealm
            validate {
                val paload = it.payload
                val email = paload.getClaim("email").asString()
                val user =  db.findUserByEmail(email)
                user
            }
        }
    }

    routing {
        UserRoutes(db, jwtService, hashFunction)
        TaskRoutes(db,hashFunction)
    }

    configureSecurity()
    configureSerialization()
    configureRouting()
}
