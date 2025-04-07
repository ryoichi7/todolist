package com.ryoichidoto.todolist.web.user

import com.ryoichidoto.todolist.service.user.ReadUserDto
import com.ryoichidoto.todolist.service.user.UserNotFoundException
import com.ryoichidoto.todolist.service.user.UserService
import com.ryoichidoto.todolist.web.core.API_PREFIX
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    path = ["$API_PREFIX/users"],
    produces = [MediaType.APPLICATION_JSON_VALUE],
)
class UserController(
    private val userService: UserService,
) {
    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
    ): ResponseEntity<ReadUserDto> {
        try {
            val userDto = userService.getUserById(id)
            return ResponseEntity.ok(userDto)
        } catch (e: UserNotFoundException) {
            return ResponseEntity.notFound().build()
        }
    }
}
