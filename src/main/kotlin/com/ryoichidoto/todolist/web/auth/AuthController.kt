package com.ryoichidoto.todolist.web.auth

import com.ryoichidoto.todolist.service.jwt.JwtService
import com.ryoichidoto.todolist.service.user.CreateUserDto
import com.ryoichidoto.todolist.service.user.ReadUserDto
import com.ryoichidoto.todolist.service.user.UserService
import com.ryoichidoto.todolist.web.core.API_PREFIX
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["$API_PREFIX/auth"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class AuthController(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: CreateUserDto,
    ): ResponseEntity<ReadUserDto> {
        val userDto = userService.createUser(request)
        return ResponseEntity.ok(userDto)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): ResponseEntity<TokenResponse> {
        val auth = UsernamePasswordAuthenticationToken(request.email, request.password)
        authenticationManager.authenticate(auth)

        val token = jwtService.generateToken(request.email)
        return ResponseEntity.ok(TokenResponse(token))
    }
}
