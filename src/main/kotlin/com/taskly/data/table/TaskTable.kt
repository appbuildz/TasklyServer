package com.taskly.data.table

import org.jetbrains.exposed.sql.Table

object TaskTable  : Table() {

    val id = varchar("id", 512)
    val userEmail = varchar("userEmail", 512).references(UserTable.email)
    val title = text("title")
    val description = text("description")
    val date = long("date")

    override val primaryKey: PrimaryKey = PrimaryKey(id)

}