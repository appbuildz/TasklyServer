package com.taskly.routes

import com.taskly.Repository.Repo
import com.taskly.authentication.JWTService
import com.taskly.data.model.LoginRequest
import com.taskly.data.model.RegisterRequest
import com.taskly.data.model.SimpleResponse
import com.taskly.data.model.User
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.post

const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/user"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"

@Location(LOGIN_REQUEST)
class UserLoginRoutes

@Location(REGISTER_REQUEST)
class UserRegisterRoutes

fun Route.UserRoutes(
    db: Repo,
    jwtService:JWTService,
    hashFunction: (String) -> String
) {

    get("/new") {
        call.respondText("Welcome to Taskly API!", ContentType.Text.Plain)
    }

    post(REGISTER_REQUEST) {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Some Fields"))
            return@post
        }

        try {
            val user = User(registerRequest.email, hashFunction(registerRequest.password), registerRequest.name)
            db.addUser(user)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
        }
    }


    post(LOGIN_REQUEST) {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Some Fields"))
            return@post
        }

        try {
            val user = db.findUserByEmail(loginRequest.email)

            if (user == null) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong Email Id"))
            } else {

                if (user.hashPassword == hashFunction(loginRequest.password)) {
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.generateToken(user)))
                } else {
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Password Incorrect!"))
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
        }
    }
}
