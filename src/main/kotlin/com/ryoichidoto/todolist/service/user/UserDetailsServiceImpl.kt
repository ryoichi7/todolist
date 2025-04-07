package com.ryoichidoto.todolist.service.user

import com.ryoichidoto.todolist.dao.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository,
) : UserDetailsService {
    /**
     * Ищет пользователя по email, так как username не уникален
     * @param email - email по которому достаем пользователя
     */
    override fun loadUserByUsername(email: String): UserDetails =
        userRepository.findByEmail(email) ?: throw UserNotFoundException.byEmail(email)
}
