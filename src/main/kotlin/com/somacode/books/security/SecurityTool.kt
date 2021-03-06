package com.somacode.books.security

import com.somacode.books.config.exception.UnauthorizedException
import com.somacode.books.entity.Authority
import com.somacode.books.service.AuthorityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class SecurityTool {

    @Autowired lateinit var authorityService: AuthorityService

    companion object {
        fun getAllAuthorities(): Collection<GrantedAuthority>? {
            val authorities = ArrayList<GrantedAuthority>()
            for (authority in Authority.Role.values()) {
                authorities.add(SimpleGrantedAuthority(authority.toString()))
            }
            return authorities
        }
    }

    fun isAdmin(): Boolean {
        return authorityService.hasRole(Authority.Role.ADMIN)
    }

    fun isUser(id: Long): Boolean {
        return id == getCustomUserDetails().id
    }

    fun isAuthenticated(): Boolean {
        return if (SecurityContextHolder.getContext().authentication == null) {
            false
        } else SecurityContextHolder.getContext().authentication.principal is CustomUserDetailsService.CustomUserDetails
    }

    fun getUserId(): Long {
        if (!isAuthenticated()) {
            throw UnauthorizedException()
        }
        return getCustomUserDetails().id!!
    }

    private fun getCustomUserDetails(): CustomUserDetailsService.CustomUserDetails {
        return SecurityContextHolder.getContext().authentication.principal as CustomUserDetailsService.CustomUserDetails
    }

}