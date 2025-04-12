package com.ryoichidoto.todolist.web.user

import com.ryoichidoto.todolist.service.user.CreateUserDto
import com.ryoichidoto.todolist.service.user.ReadUserDto
import com.ryoichidoto.todolist.service.user.UpdateUserDto
import com.ryoichidoto.todolist.service.user.UserService
import com.ryoichidoto.todolist.web.core.API_PREFIX
import com.ryoichidoto.todolist.web.core.Messages
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
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
    fun getUser(
        @PathVariable id: Long,
    ): ResponseEntity<ReadUserDto> {
        val userDto = userService.getUserById(id)
        return ResponseEntity.ok(userDto)
    }

    @PostMapping
    fun createUser(
        @Valid @RequestBody request: CreateUserDto,
    ): ResponseEntity<ReadUserDto> {
        val userDto = userService.createUser(request)
        return ResponseEntity.ok(userDto)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody userToUpdate: UpdateUserDto,
    ): ResponseEntity<ReadUserDto> {
        val updatedUser = userService.updateUser(id, userToUpdate)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: Long,
    ): ResponseEntity<String> {
        userService.deleteUser(id)
        return ResponseEntity.ok(Messages.USER_DELETED_MESSAGE)
    }
}
