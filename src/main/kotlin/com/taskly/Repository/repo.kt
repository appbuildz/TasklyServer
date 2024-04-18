package com.taskly.Repository

import com.taskly.Repository.DatabaseFactory.dbQuery
import com.taskly.data.model.User
import com.taskly.data.table.userTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class repo {

    suspend fun addUser(user: User) {
        dbQuery{
            userTable.insert { ut->
                ut[userTable.email] = user.email
                ut[userTable.password] = user.hashPassword
                ut[userTable.userName] = user.username

            }
        }
    }

    suspend fun findUserByEmail(email: String)= dbQuery {
        userTable.select { userTable.email eq email }
            .map { rowToUser(it) }
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow?): User?{
        if(row == null){
            return null
        }

        return User(
            email = row[userTable.email],
            hashPassword = row[userTable.password],
            username = row[userTable.userName]
        )
    }

}