package com.ryoichidoto.todolist.web.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ExceptionHandler


@Component
class DefaultExceptionHandler {

    @ExceptionHandler
    fun handleAllExceptions(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong")
    }
}