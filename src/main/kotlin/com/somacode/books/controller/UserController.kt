package com.somacode.books.controller

import com.somacode.books.entity.Authority
import com.somacode.books.entity.User
import com.somacode.books.security.SecurityTool
import com.somacode.books.service.UserService
import com.somacode.books.service.tool.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.Email
import javax.validation.constraints.Size

@Validated
@RestController
class UserController {

    @Autowired lateinit var userService: UserService
    @Autowired lateinit var securityTool: SecurityTool

    @PostMapping("/public/users/register")
    fun postRegister(
            @RequestParam @Email email: String,
            @RequestParam @Size(min = 4) password: String,
            @RequestParam @Size(min = 2) name: String,
            @RequestParam @Size(min = 2) lastname: String,
    ): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                userService.register(email, password, name, lastname, null)
        )
    }

    @PreAuthorize("@securityTool.isUser(#id)")
    @PatchMapping("/api/users/{id}")
    fun patchUser(
            @PathVariable id: Long,
            @RequestBody user: User
    ): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(
                userService.update(id, user)
        )
    }

    @PreAuthorize("@securityTool.isAdmin()")
    @GetMapping("/api/users")
    fun getUsers(
            @RequestParam(required = false) search: String?,
            @RequestParam page: Int,
            @RequestParam size: Int,
            @RequestParam role: Authority.Role
    ): ResponseEntity<List<User>> {
        val result: Page<User> = userService.findFilterPageable(page, size, search, role)
        return ResponseEntity.status(HttpStatus.OK)
                .header(Constants.X_TOTAL_COUNT_HEADER, result.totalElements.toString())
                .body(result.content)
    }

    @PreAuthorize("@securityTool.isAdmin()")
    @GetMapping("/api/users/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id))
    }

    @GetMapping("/api/users/@me")
    fun getMyUser(): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getMyUser())
    }

    @PreAuthorize("@securityTool.isAdmin()")
    @DeleteMapping("/api/users/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.delete(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
    }

    @PreAuthorize("@securityTool.isUser(#id)")
    @PatchMapping("/api/users/{id}/change-password")
    fun patchUserChangePassword(
            @PathVariable id: Long,
            @RequestParam password: String,
            @RequestParam newPassword: String
    ): ResponseEntity<Void> {
        userService.changePassword(id, password, newPassword)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
    }

    @PreAuthorize("@securityTool.isUser(#id)")
    @PatchMapping("/api/users/{id}/avatar/update")
    fun patchUserAvatar(
            @PathVariable id: Long,
            @RequestParam avatarFile: MultipartFile,
    ): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateAvatar(id, avatarFile))
    }

    @PreAuthorize("@securityTool.isUser(#id)")
    @DeleteMapping("/api/users/{id}/avatar/delete")
    fun deleteUserAvatar(@PathVariable id: Long): ResponseEntity<User> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteAvatar(id))
    }

    @PreAuthorize("@securityTool.isAdmin()")
    @PatchMapping("/api/users/{id}/activate")
    fun patchUserActivate(@PathVariable id: Long, @RequestParam active: Boolean): ResponseEntity<Void> {
        if (active) userService.activate(id) else userService.deactivate(id)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)
    }

}