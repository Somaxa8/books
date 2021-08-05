package com.somacode.books.controller

import com.somacode.books.entity.Book
import com.somacode.books.security.SecurityTool
import com.somacode.books.service.BookService
import com.somacode.books.service.tool.Constants
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@RestController
class BookController {

    @Autowired lateinit var bookService: BookService
    @Autowired lateinit var securityTool: SecurityTool

    @PostMapping("/api/books")
    fun postBook(
            @RequestParam title: String,
            @RequestParam(required = false) author: String?,
            @RequestParam(required = false) date: LocalDate?,
            @RequestParam languageId: Long,
            @RequestParam(required = false) editorial: String?,
            @RequestParam(required = false) description: String?,
            @RequestParam bookFile: MultipartFile
    ): ResponseEntity<Book> {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                bookService.create(title, author, date, languageId, editorial, description, bookFile)
        )
    }

    @PatchMapping("/api/books/{id}")
    fun patchBook(
            @PathVariable id: Long,
            @RequestParam(required = false) title: String?,
            @RequestParam(required = false) author: String?,
            @RequestParam(required = false) date: LocalDate?,
            @RequestParam(required = false) languageId: Long?,
            @RequestParam(required = false) editorial: String?,
            @RequestParam(required = false) description: String?,
            @RequestParam(required = false) bookFile: MultipartFile?
    ): ResponseEntity<Book> {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                bookService.update(id, title, author, date, languageId, editorial, description, bookFile)
        )
    }

    @GetMapping("/public/books/{id}")
    fun getBook(@PathVariable id: Long): ResponseEntity<Book> {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findById(id))
    }

    @GetMapping("/public/books")
    fun getBooks(
            @RequestParam(required = false) search: String?,
            @RequestParam page: Int,
            @RequestParam size: Int
    ): ResponseEntity<List<Book>> {
        val result = bookService.findFilterPageable(page, size, search)
        return ResponseEntity.status(HttpStatus.OK)
                .header(Constants.X_TOTAL_COUNT_HEADER, result.totalElements.toString())
                .body(result.content)
    }

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
}