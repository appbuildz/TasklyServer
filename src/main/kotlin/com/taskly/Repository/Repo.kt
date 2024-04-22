package com.taskly.Repository

import com.taskly.Repository.DatabaseFactory.dbQuery
import com.taskly.data.model.Task
import com.taskly.data.model.User
import com.taskly.data.table.TaskTable
import com.taskly.data.table.UserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class Repo {


    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { ut ->
                ut[UserTable.email] = user.email
                ut[UserTable.password] = user.hashPassword
                ut[UserTable.userName] = user.username

            }
        }
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email eq email }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null) {
            return null
        }

        return User(
            email = row[UserTable.email],
            hashPassword = row[UserTable.password],
            username = row[UserTable.userName]
        )
    }

//    ============ Task Operations ============

    suspend fun addTask(task: Task, email: String) {
        dbQuery {
            TaskTable.insert { tt ->
                tt[TaskTable.id] = task.id
                tt[TaskTable.userEmail] = email
                tt[TaskTable.title] = task.title
                tt[TaskTable.description] = task.description
                tt[TaskTable.date] = task.date
            }
        }
    }

    suspend fun getAllTasks(email: String): List<Task> = dbQuery {

        TaskTable.select {
            TaskTable.userEmail.eq(email)
        }.mapNotNull { rowToTask(it) }

    }


    private fun rowToTask(row: ResultRow?): Task? {
        if (row == null) {
            return null
        }
        return Task(
            id = row[TaskTable.id],
            title = row[TaskTable.title],
            description = row[TaskTable.description],
            date = row[TaskTable.date],
        )
    }


    suspend fun updateTask(task: Task, email: String) {

        dbQuery {
            TaskTable.update(
                where = {
                    TaskTable.userEmail.eq(email) and TaskTable.id.eq(task.id)
                }
            ) { tt ->
                tt[TaskTable.title] = task.title
                tt[TaskTable.description] = task.description
                tt[TaskTable.date] = task.date
            }

        }

    }

    suspend fun deleteTask(id: String, email: String) {

        dbQuery {
            TaskTable.deleteWhere { TaskTable.id eq id and TaskTable.userEmail.eq(email) }
        }

    }

}