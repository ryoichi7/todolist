package com.ryoichidoto.todolist.dao.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): List<User>

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean
}
