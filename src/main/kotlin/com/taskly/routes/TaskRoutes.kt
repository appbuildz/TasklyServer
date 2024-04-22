package com.taskly.routes

import com.taskly.Repository.Repo
import com.taskly.data.model.SimpleResponse
import com.taskly.data.model.Task
import com.taskly.data.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


const val TASK = "$API_VERSION/task"
const val CREATE_TASK = "$TASK/create"
const val DELETE_TASK = "$TASK/delete"
const val UPDATE_TASK = "$TASK/update"


fun Route.TaskRoutes(
    db: Repo,
    hasFunction: (String) -> String
) {

    authenticate("jwt") {

        post(CREATE_TASK) {

            val task = try {
                call.receive<Task>()
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, ex.message ?: "Missing Field."))
                return@post
            }

            try {
                val email = call.principal<User>()!!.email
                db.addTask(task, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Successfully added task"))
            } catch (ex: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, ex.message ?: "Error during adding task.")
                )
            }

        }

        get(TASK) {
            try {
                val email = call.principal<User>()!!.email
                val task = db.getAllTasks(email)
                call.respond(HttpStatusCode.OK, task)
            } catch (ex: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, ex.message ?: "Error during getting task.")
                )
            }
        }

        post(UPDATE_TASK) {
            val task = try {
                call.receive<Task>()
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, ex.message ?: "Missing Field."))
                return@post
            }

            try {
                val email = call.principal<User>()!!.email
                db.updateTask(task, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Successfully updated task"))
            } catch (ex: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, ex.message ?: "Error during updating task.")
                )
            }
        }

        delete(DELETE_TASK) {
            val taskID = try {
                call.parameters["id"]!!
            } catch (ex: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, ex.message ?: "Error during deleting task.")
                )
                return@delete
            }

            try {
                val email = call.principal<User>()!!.email
                db.deleteTask(taskID, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Successfully deleted task"))
            } catch (ex: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    SimpleResponse(false, ex.message ?: "Error during deleting task.")
                )
            }

        }
    }


}