package com.somacode.books.security

import com.somacode.books.security.SecurityTool
import com.somacode.books.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookSecurity {

    @Autowired lateinit var bookService: BookService
    @Autowired lateinit var securityTool: SecurityTool

    fun isOwn(id: Long): Boolean {
        return bookService.existsByIdAndCreatedBy_Id(id, securityTool.getUserId())
    }

}