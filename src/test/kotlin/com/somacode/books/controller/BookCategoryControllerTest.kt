package com.somacode.books.controller

import com.somacode.books.entity.BookCategory
import com.somacode.books.service.OAuthService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
internal class BookCategoryControllerTest {

    @Autowired lateinit var restTemplate: TestRestTemplate
    @Autowired lateinit var oAuthService: OAuthService

    @Test
    @DisplayName("GET when get BookCategory list, then returns 200")
    fun whenGetBookCategoryList_thenReturns200() {
        //when
        val responseEntity = restTemplate.getForEntity("/public/book-categories", List::class.java)
        //then
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertNotNull(responseEntity.body)
        assertFalse(responseEntity.body!!.isEmpty())
    }

    @Test
    @DisplayName("POST when post a new BookCategory, then returns 201")
    fun givenNewBookCategory_whenPostBookCategory_thenReturns201() {

        val token = oAuthService.login("admin@somacode.com", "1234").value
        val headers = HttpHeaders()
        headers.setBearerAuth(token)

        val form = LinkedMultiValueMap<String, String>()
        form.add("title", "hello")
        val request = HttpEntity(form, headers)

        //when
        val responseEntity = restTemplate.postForEntity("/api/book-categories", request, BookCategory::class.java)

        //then
        assertEquals(HttpStatus.CREATED, responseEntity.statusCode)
        assertNotNull(responseEntity.body)
        assertNotNull(responseEntity.body!!.id)
        assertEquals(responseEntity.body!!.title, "hello")
    }

    @Test
    @DisplayName("PATCH when patch a BookCategory, then returns 200")
    fun givenBookCategory_whenPatchBookCategory_thenReturns200() {

        val token = oAuthService.login("admin@somacode.com", "1234").value
        val headers = HttpHeaders()
        headers.setBearerAuth(token)

        val request = HttpEntity(BookCategory(title = "bye"), headers)

        //when
        val responseEntity = restTemplate.exchange("/api/book-categories/1", HttpMethod.PATCH, request, BookCategory::class.java)

        //then
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertNotNull(responseEntity.body)
        assertNotNull(responseEntity.body!!.id)
        assertEquals(responseEntity.body!!.title, "bye")
    }

}