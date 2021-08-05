package com.somacode.books.controller

import com.somacode.books.entity.BookCategory
import com.somacode.books.service.BookCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BookCategoryController {

    @Autowired lateinit var bookCategoryService: BookCategoryService

    @PostMapping("/api/book-categories")
    fun postBookCategory(@RequestParam title: String): ResponseEntity<BookCategory> {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookCategoryService.create(title))
    }

    @GetMapping("/public/book-categories")
    fun getBookCategories(): ResponseEntity<List<BookCategory>> {
        return ResponseEntity.status(HttpStatus.OK).body(bookCategoryService.findAll())
    }

    @PatchMapping("/api/book-categories/{id}")
    fun patchBookCategory(@PathVariable id: Long, @RequestBody request: BookCategory): ResponseEntity<BookCategory> {
        return ResponseEntity.status(HttpStatus.OK).body(bookCategoryService.update(id, request))
    }

    @DeleteMapping("/api/book-categories/{id}")
    fun deleteBookCategory(@PathVariable id: Long): ResponseEntity<Void> {
        bookCategoryService.delete(id)
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

    @PatchMapping("/api/book/{bookId}/book-categories/{id}/relate")
    fun patchBookRelateBookCategory(@PathVariable bookId: Long, @PathVariable id: Long): ResponseEntity<Void> {
        bookCategoryService.relateCategory(id, bookId)
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

    @PatchMapping("/api/book/{bookId}/book-categories/{id}/unrelate")
    fun patchBookUnrelateBookCategory(@PathVariable bookId: Long, @PathVariable id: Long): ResponseEntity<Void> {
        bookCategoryService.unrelateCategory(id, bookId)
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

}