package com.taskly.data.table

import org.jetbrains.exposed.sql.Table

object userTable : Table() {

    val email = varchar("email",512)
    val password = varchar("password",512)
    val userName = varchar("username", 512)

    override val primaryKey: PrimaryKey = PrimaryKey(email)
}