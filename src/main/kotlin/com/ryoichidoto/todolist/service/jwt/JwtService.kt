package com.ryoichidoto.todolist.service.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date

@Service
class JwtService(
    @Value("\${jwt.secret}")
    private val secret: String,
) {
    private val algorithm = Algorithm.HMAC256(secret)
    private val verifier = JWT.require(algorithm).build()

    fun generateToken(email: String): String {
        val now = Instant.now()
        val expiresAt = now.plus(1, ChronoUnit.HOURS)

        return JWT
            .create()
            .withSubject(email)
            .withIssuedAt(Date.from(now))
            .withExpiresAt(Date.from(expiresAt))
            .sign(algorithm)
    }

    fun validateToken(token: String): Boolean {
        val decodedJWT = verifier.verify(token)
        return decodedJWT.expiresAt.toInstant().isAfter(Instant.now())
    }

    fun extractEmail(token: String): String {
        val decodedJWT = verifier.verify(token)
        return decodedJWT.subject
    }
}
