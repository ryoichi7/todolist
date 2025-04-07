package com.ryoichidoto.todolist.dao.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false, length = 50)
    private var username: String,
    @Column(nullable = false, unique = true, length = 100)
    var email: String,
    @Column(nullable = false, length = 100)
    private var password: String,
    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = LocalDateTime.now(),
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    override fun getUsername(): String = username

    override fun getPassword(): String = password

    @PrePersist
    fun onPrePersist() {
        val now = LocalDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onPreUpdate() {
        updatedAt = LocalDateTime.now()
    }

    fun setUsername(newUsername: String) {
        this.username = newUsername
    }

    fun setPassword(newPassword: String) {
        this.password = newPassword
    }
}
