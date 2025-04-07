package com.ryoichidoto.todolist.service.user

import com.ryoichidoto.todolist.dao.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userMapper: UserMapper,
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = false)
    fun createUser(dto: CreateUserDto): ReadUserDto {
        requireUniqueEmail(dto.email)

        val user = userMapper.fromCreateDto(dto)
        val saved = userRepository.save(user)

        return userMapper.toReadDto(saved)
    }

    fun getAllUsers(): List<ReadUserDto> = userRepository.findAll().map { userMapper.toReadDto(it) }

    fun getUsersByUsername(username: String): List<ReadUserDto> =
        userRepository
            .findByUsername(username)
            .map { userMapper.toReadDto(it) }

    fun getUserById(id: Long): ReadUserDto {
        val user =
            userRepository
                .findById(id)
                .orElseThrow { UserNotFoundException.byId(id) }

        return userMapper.toReadDto(user)
    }

    @Transactional(readOnly = false)
    fun updateUser(
        id: Long,
        dto: UpdateUserDto,
    ): ReadUserDto {
        val existing =
            userRepository
                .findById(id)
                .orElseThrow { UserNotFoundException.byId(id) }

        if (dto.email != null && dto.email != existing.email) {
            requireUniqueEmail(dto.email)
        }
        userMapper.applyUpdateDto(existing, dto)
        val updated = userRepository.save(existing)
        return userMapper.toReadDto(updated)
    }

    @Transactional(readOnly = false)
    fun deleteUser(id: Long) {
        val user =
            userRepository
                .findById(id)
                .orElseThrow { UserNotFoundException.byId(id) }
        userRepository.delete(user)
    }

    private fun requireUniqueEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            throw UserAlreadyExistsException(email)
        }
    }
}
