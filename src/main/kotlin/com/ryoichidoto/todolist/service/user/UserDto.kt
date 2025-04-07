package com.ryoichidoto.todolist.service.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserDto(
    @field:NotBlank(message = "Username is required.")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    val username: String,
    @field:NotBlank(message = "Email is required.")
    @field:Email(message = "Invalid email format.")
    val email: String,
    @field:NotBlank(message = "Password is required.")
    @field:Size(min = 6, max = 100, message = "Password must be at least 6 characters.")
    val password: String
)

data class UpdateUserDto(
    val id: Long? = null,
    @field:NotBlank(message = "Username is required.")
    @field:Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    val username: String? = null,
    @field:NotBlank(message = "Email is required.")
    @field:Email(message = "Invalid email format.")
    val email: String? = null,
    @field:NotBlank(message = "Password is required.")
    @field:Size(min = 6, max = 100, message = "Password must be at least 6 characters.")
    val password: String? = null,
)

data class ReadUserDto(
    val id: Long,
    val username: String,
    val email: String
)
