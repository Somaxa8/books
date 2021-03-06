package com.somacode.books.controller

import com.somacode.books.entity.Book
import com.somacode.books.security.SecurityTool
import com.somacode.books.service.BookService
import com.somacode.books.security.BookSecurity
import com.somacode.books.service.tool.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@RestController
class BookController {

    @Autowired lateinit var bookService: BookService
    @Autowired lateinit var securityTool: SecurityTool
    @Autowired lateinit var bookSecurity: BookSecurity

    @PostMapping("/api/books")
    fun postBook(
            @RequestParam title: String,
            @RequestParam(required = false) author: String?,
            @RequestParam(required = false) date: LocalDate?,
            @RequestParam languageId: Long,
            @RequestParam categoryIds: List<Long>,
            @RequestParam(required = false) editorial: String?,
            @RequestParam(required = false) description: String?,
            @RequestParam bookFile: MultipartFile
    ): ResponseEntity<Book> {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                bookService.create(title, author, date, languageId, categoryIds, editorial, description, bookFile)
        )
    }

    @PreAuthorize("@bookSecurity.isOwn(#id) or @securityTool.isAdmin()")
    @PatchMapping("/api/books/{id}")
    fun patchBook(
            @PathVariable id: Long,
            @RequestParam(required = false) title: String?,
            @RequestParam(required = false) author: String?,
            @RequestParam(required = false) date: LocalDate?,
            @RequestParam(required = false) languageId: Long?,
            @RequestParam categoryIds: List<Long>,
            @RequestParam(required = false) editorial: String?,
            @RequestParam(required = false) description: String?,
            @RequestParam(required = false) bookFile: MultipartFile?
    ): ResponseEntity<Book> {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookService.update(id, title, author, date, languageId, categoryIds, editorial, description, bookFile)
        )
    }

    @PreAuthorize("@securityTool.isUser(#userId) or @securityTool.isAdmin()")
    @GetMapping("/api/@me/users/{userId}/books")
    fun getMyBooks(
            @PathVariable userId: Long,
            @RequestParam(required = false) search: String?,
            @RequestParam(required = false) categoryId: Long?,
            @RequestParam(required = false) start: LocalDate?,
            @RequestParam(required = false) end: LocalDate?,
            @RequestParam page: Int,
            @RequestParam size: Int
    ): ResponseEntity<List<Book>> {
        val result = bookService.findFilterPageable(page, size, search, categoryId, userId, start, end, null)
        return ResponseEntity.status(HttpStatus.OK)
                .header(Constants.X_TOTAL_COUNT_HEADER, result.totalElements.toString())
                .body(result.content)
    }

    @GetMapping("/public/books/{id}")
    fun getBook(@PathVariable id: Long): ResponseEntity<Book> {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findById(id))
    }

    @GetMapping("/public/books")
    fun getBooks(
            @RequestParam(required = false) search: String?,
            @RequestParam(required = false) categoryId: Long?,
            @RequestParam(required = false) start: LocalDate?,
            @RequestParam(required = false) end: LocalDate?,
            @RequestParam page: Int,
            @RequestParam size: Int
    ): ResponseEntity<List<Book>> {
        val result = bookService.findFilterPageable(page, size, search, categoryId, null, start, end, null)
        return ResponseEntity.status(HttpStatus.OK)
                .header(Constants.X_TOTAL_COUNT_HEADER, result.totalElements.toString())
                .body(result.content)
    }

    @PreAuthorize("@securityTool.isUser(#userId) or @securityTool.isAdmin()")
    @GetMapping("/api/users/{userId}/books/favorites")
    fun getFavoriteBooks(
            @PathVariable userId: Long,
            @RequestParam(required = false) search: String?,
            @RequestParam(required = false) categoryId: Long?,
            @RequestParam page: Int,
            @RequestParam size: Int
    ): ResponseEntity<List<Book>> {
        val result = bookService.findFilterPageable(page, size, search, categoryId, null, null, null, userId)
        return ResponseEntity.status(HttpStatus.OK)
                .header(Constants.X_TOTAL_COUNT_HEADER, result.totalElements.toString())
                .body(result.content)
    }

    @PreAuthorize("@bookSecurity.isOwn(#id) or @securityTool.isAdmin()")
    @DeleteMapping("/api/books/{id}")
    fun deleteBook(@PathVariable id: Long): ResponseEntity<Void> {
        bookService.delete(id)
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

    @PatchMapping("/api/books/{id}/favorite")
    fun favoriteBook(@PathVariable id: Long): ResponseEntity<Void> {
        bookService.addFavorite(id, securityTool.getUserId())
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

    @PatchMapping("/api/books/{id}/unfavorite")
    fun unfavoriteBook(@PathVariable id: Long): ResponseEntity<Void> {
        bookService.removeFavorite(id, securityTool.getUserId())
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

    @PreAuthorize("@securityTool.isAdmin()")
    @PostMapping("/api/synchronize/books")
    fun synchronizeBook(
            @RequestParam title: String,
            @RequestParam author: String,
            @RequestParam category: String,
            @RequestParam editorial: String,
            @RequestParam description: String,
            @RequestParam bookFile: MultipartFile,
            @RequestParam coverFile: MultipartFile
    ): ResponseEntity<Book> {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                bookService.synchronizeBook(title, author, category, editorial, description, bookFile, coverFile)
        )
    }
}