package com.taskly.data.model

import io.ktor.server.auth.*

data class User(
    val email: String,
    val hashPassword: String,
    val username: String
) : Principal
