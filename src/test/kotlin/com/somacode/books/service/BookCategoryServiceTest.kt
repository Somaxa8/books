package com.somacode.books.service

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest
internal class BookCategoryServiceTest {

    @Autowired lateinit var bookCategoryService: BookCategoryService

    @Test
    @DisplayName("when create BookCategory it must return valid BookCategory")
    fun create() {

        // when
        val bookCategory = bookCategoryService.create("Titulo")

        //then
        assertNotNull(bookCategory.id)
        assertEquals(bookCategory.title, "Titulo")

    }
}