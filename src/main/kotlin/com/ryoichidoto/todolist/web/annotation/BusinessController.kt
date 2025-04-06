package com.ryoichidoto.todolist.web.annotation

import com.ryoichidoto.todolist.web.core.API_PREFIX
import org.springframework.core.annotation.AliasFor
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@RestController
@RequestMapping
annotation class BusinessController(
    @get:AliasFor(annotation = RequestMapping::class, attribute = "value")
    vararg val value: String = [API_PREFIX]
)
