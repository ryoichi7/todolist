package com.ryoichidoto.todolist.web.core

import com.ryoichidoto.todolist.service.jwt.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null ||
            !authHeader.startsWith("Bearer ") ||
            SecurityContextHolder.getContext().authentication != null
        ) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)
        if (jwtService.validateToken(jwt)) {
            val email = jwtService.extractEmail(jwt)
            val userDetails = userDetailsService.loadUserByUsername(email)

            val authToken =
                UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities,
                )

            SecurityContextHolder.getContext().authentication = authToken
        }

        filterChain.doFilter(request, response)
    }
}
