package com.ryoichidoto.todolist.web.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@ControllerAdvice
class DefaultExceptionHandler {
    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthEx(
        ex: AuthenticationException,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val error =
            ErrorResponse(
                timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                status = HttpStatus.UNAUTHORIZED.value(),
                error = HttpStatus.UNAUTHORIZED.reasonPhrase,
                message = "Invalid credentials",
                path = request.requestURI,
            )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val error =
            ErrorResponse(
                timestamp = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                message = ex.message ?: "Unexpected error",
                path = request.requestURI,
            )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }
}
