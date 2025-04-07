package com.ryoichidoto.todolist.web.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotBlank(message = "Email is required.")
    @field:Email(message = "Invalid email format.")
    val email: String,
    @field:NotBlank(message = "Password is required.")
    @field:Size(min = 6, max = 100, message = "Password must be at least 6 characters.")
    val password: String
)

data class TokenResponse(
    val token: String
)