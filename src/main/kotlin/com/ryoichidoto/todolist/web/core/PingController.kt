package com.ryoichidoto.todolist.web.core

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(API_PREFIX)
class PingController {
    @GetMapping("/ping")
    fun ping(): ResponseEntity<String> = ResponseEntity.ok().body("pong")
}
