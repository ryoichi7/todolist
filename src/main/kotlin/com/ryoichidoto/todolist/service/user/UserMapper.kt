package com.ryoichidoto.todolist.service.user

import com.ryoichidoto.todolist.dao.user.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val passwordEncoder: PasswordEncoder,
) {
    fun fromCreateDto(dto: CreateUserDto): User =
        User(
            username = dto.username,
            email = dto.email,
            password = passwordEncoder.encode(dto.password),
        )

    fun applyUpdateDto(
        existing: User,
        dto: UpdateUserDto,
    ): User {
        dto.username?.let { existing.username = it }
        dto.email?.let { existing.email = it }
        dto.password?.let { existing.password = passwordEncoder.encode(it) }

        return existing
    }

    fun toReadDto(user: User): ReadUserDto =
        ReadUserDto(
            id = user.id ?: -1L,
            username = user.username,
            email = user.email,
        )
}
