package com.somacode.books.service

import com.somacode.books.config.exception.BadRequestException
import com.somacode.books.config.exception.NotFoundException
import com.somacode.books.entity.Authority
import com.somacode.books.entity.Document
import com.somacode.books.entity.User
import com.somacode.books.repository.UserRepository
import com.somacode.books.repository.criteria.UserCriteria
import com.somacode.books.security.SecurityTool
import com.somacode.books.service.tool.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import javax.transaction.Transactional

@Service
@Transactional
class UserService {

    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var userCriteria: UserCriteria
    @Autowired lateinit var authorityService: AuthorityService
    @Autowired lateinit var documentService: DocumentService
    @Autowired lateinit var passwordEncoder: PasswordEncoder
    @Autowired lateinit var securityTool: SecurityTool
    @Value("\${custom.username}") lateinit var username: String
    @Value("\${custom.password}") lateinit var password: String


    fun init() {
        if (userRepository.count() <= 0) {
            println("UserService init()")

            register(
                    email = "admin@somacode.com",
                    password = "1234",
                    name = "Administrador",
                    avatar = null
            )
            authorityService.relateUser(Authority.Role.ADMIN, 2)
        }
    }

    fun register(email: String, password: String, name: String, avatar: MultipartFile?): User {
        if (userRepository.existsByEmail(email)) {
            throw BadRequestException("Email already exists")
        }
        if (password.isBlank() || password.length < Constants.PASSWORD_MIN_SIZE) {
            throw BadRequestException("Password must be greater than 4 characters")
        }

        val user = User(
                email = email,
                password = passwordEncoder.encode(password),
                name = name,
                active = true
        )

        avatar?.let {
            user.avatar = documentService.create(it, Document.Type.IMAGE, User::class.java.simpleName)
        }

        userRepository.save(user)

        authorityService.relateUser(Authority.Role.USER, user.id!!)

        return user
    }

    fun update(id: Long, request: User): User {
        val user = findById(id)

        request.name?.let { user.name = it }
        request.lastname?.let { user.lastname = it }

        return userRepository.save(user)
    }

    fun existsById(id: Long): Boolean {
        return userRepository.existsById(id)
    }

    fun existsByEmailAndActiveTrue(email: String): Boolean {
        return userRepository.existsByEmailAndActiveTrue(email)
    }

    fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    fun findFilterPageable(page: Int, size: Int, search: String?, role: Authority.Role): Page<User> {
        return userCriteria.findFilterPageable(page, size, search, role)
    }

    fun findById(id: Long): User {
        if (!userRepository.existsById(id)) {
            throw NotFoundException()
        }
        return userRepository.getOne(id)
    }

    fun findAllById(ids: List<Long>): List<User> {
        if (userRepository.countByIdIn(ids) != ids.size) {
            throw NotFoundException()
        }
        return userRepository.findAllById(ids)
    }

    fun findByEmail(email: String): User {
        if (!userRepository.existsByEmail(email)) {
            throw NotFoundException()
        }
        return userRepository.findByEmail(email)
    }

    fun findAll(): List<User> {
        return userRepository.findAll()
    }

    fun delete(id: Long) {
        if (!userRepository.existsById(id)) {
            throw NotFoundException()
        }
        userRepository.deleteById(id)
    }

    fun getMyUser(): User {
        return findById(securityTool.getUserId())
    }

    fun changePassword(id: Long, password: String, newPassword: String) {
        val user = findById(id)
        if (!passwordEncoder.matches(password, user.password)) {
            throw BadRequestException()
        }
        if (newPassword.isBlank() || newPassword.length < Constants.PASSWORD_MIN_SIZE) {
            throw BadRequestException()
        }
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
    }

}