package com.somacode.books.controller

import com.somacode.books.entity.BookStar
import com.somacode.books.security.SecurityTool
import com.somacode.books.service.BookStarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class BookStarController {

    @Autowired lateinit var bookStarService: BookStarService
    @Autowired lateinit var securityTool: SecurityTool

    @PatchMapping("/api/books/{bookId}/book-stars")
    fun patchBookStar(@PathVariable bookId: Long, @RequestParam count: Int): ResponseEntity<BookStar> {
        return ResponseEntity.status(HttpStatus.OK).body(
                bookStarService.bookStar(securityTool.getUserId(), bookId, count)
        )
    }
}