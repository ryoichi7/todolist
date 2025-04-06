package com.ryoichidoto.todolist

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<TodolistApplication>().with(TestcontainersConfiguration::class).run(*args)
}
