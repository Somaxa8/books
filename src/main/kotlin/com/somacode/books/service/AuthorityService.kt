package com.somacode.books.service

import com.somacode.books.config.exception.NotFoundException
import com.somacode.books.entity.Authority
import com.somacode.books.repository.AuthorityRepository
import com.somacode.books.security.SecurityTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class AuthorityService {

    @Autowired lateinit var authorityRepository: AuthorityRepository
    @Autowired lateinit var userService: UserService
    @Autowired lateinit var securityTool: SecurityTool


    fun init() {
        if (authorityRepository.count() <= 0) {
            println("AuthorityService init()")
            for (role in Authority.Role.values()) {
                val authority = Authority()
                authority.role = role
                authority.title = role.toString().replace("_", " ").toLowerCase()
                authorityRepository.save(authority)
            }
        }
    }

    fun relateUser(role: Authority.Role, userId: Long) {
        val authority = findByRole(role)
        val user = userService.findById(userId)
        authority.users.add(user)
    }

    fun unrelateUser(role: Authority.Role, userId: Long) {
        val authority = findByRole(role)
        val user = userService.findById(userId)
        authority.users.remove(user)
    }

    fun findByRole(role: Authority.Role): Authority {
        if (!authorityRepository.existsById(role)) {
            throw NotFoundException()
        }
        return authorityRepository.getOne(role)
    }

    fun findAll(): List<Authority> {
        return authorityRepository.findAll()
    }

    fun findByUserId(userId: Long): List<Authority> {
        val roles = authorityRepository.findByUsers_Id(userId).map { it.role }

        return authorityRepository.findAll().onEach {
            it.enabled = roles.contains(it.role)
        }
    }

    fun hasRole(role: Authority.Role): Boolean {
        val roles = authorityRepository.findByUsers_Id(securityTool.getUserId()).map { it.role }
        return roles.contains(role)
    }

}