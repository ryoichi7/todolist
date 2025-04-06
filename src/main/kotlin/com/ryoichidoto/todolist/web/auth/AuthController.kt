package com.ryoichidoto.todolist.web.auth

import com.ryoichidoto.todolist.web.annotation.BusinessController
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@BusinessController("/auth")
class AuthController {

    @PostMapping("/register")
    fun register(): ResponseEntity<String> {
        TODO()
    }

    @GetMapping("/login")
    fun login(): ResponseEntity<String> {
        TODO()
    }

    @GetMapping("/logout")
    fun logout(): ResponseEntity<String> {
        TODO()
    }
}