package com.taskly.data.table

import org.jetbrains.exposed.sql.Table

object CategoryTable:Table() {

    val categoryId = varchar("categoryId", 512)
    val categoryName = varchar("categoryName", 512)

}