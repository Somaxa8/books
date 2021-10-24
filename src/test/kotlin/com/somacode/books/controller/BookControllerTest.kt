package com.somacode.books.controller

import com.somacode.books.entity.Book
import com.somacode.books.service.OAuthService
import com.somacode.books.service.tool.MockTool
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.core.annotation.Order
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
internal class BookControllerTest {

    @Autowired lateinit var restTemplate: TestRestTemplate
    @Autowired lateinit var oAuthService: OAuthService
    @Autowired lateinit var mockTool: MockTool


    @Test
    @DisplayName("POST when post a new Book, then returns 201")
    fun givenNewBook_whenPostBook_thenReturns201() {

        val token = oAuthService.login("admin@somacode.com", "1234").value
        val headers = HttpHeaders()
        headers.setBearerAuth(token)
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val form = LinkedMultiValueMap<String, Any>()
        form.add("title", "hello")
        form.add("languageId", 1)
        form.add("categoryIds", "1,2")
        form.add("bookFile", mockTool.multipartFilePdf().resource)
        val request = HttpEntity(form, headers)

        //when
        val responseEntity = restTemplate.postForEntity("/api/books", request, Book::class.java)

        //then
        assertEquals(HttpStatus.CREATED, responseEntity.statusCode)
        assertNotNull(responseEntity.body)
        assertNotNull(responseEntity.body!!.id)
        assertEquals(responseEntity.body!!.title, "hello")
        assertNotNull(responseEntity.body!!.book)
    }

    @Test
    @DisplayName("PATCH when patch a Book, then returns 200")
    fun givenBook_whenPatchBook_thenReturns200() {

        val token = oAuthService.login("admin@somacode.com", "1234").value
        val headers = HttpHeaders()
        headers.setBearerAuth(token)
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        val form = LinkedMultiValueMap<String, Any>()
        form.add("title", "hello2")
        form.add("languageId", 1)
        form.add("categoryIds", "1,2")
        form.add("bookFile", mockTool.multipartFilePdf().resource)
        val request = HttpEntity(form, headers)

        //when
        val responseEntity = restTemplate.exchange("/api/books/1", HttpMethod.PATCH, request, Book::class.java)

        //then
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertNotNull(responseEntity.body)
        assertNotNull(responseEntity.body!!.id)
        assertEquals(responseEntity.body!!.title, "hello2")
        assertNotNull(responseEntity.body!!.book)
    }

    @Test
    @DisplayName("GET given Book id, when GET existing Book, then returns 200")
    fun givenBookId_whenGetNonExistingBookCategory_thenReturns200() {
        //when
        val responseEntity = restTemplate.getForEntity("/public/books/1", Book::class.java)

        //then
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertNotNull(responseEntity.body)
        assertNotNull(responseEntity.body!!.id)
        assertNotNull(responseEntity.body!!.title)
        assertNotNull(responseEntity.body!!.book)
    }

    @Test
    @DisplayName("GET when get Book list, then returns 200")
    fun whenGetBookList_thenReturns200() {
        //when
        val responseEntity = restTemplate.getForEntity("/public/books?page=0&size=5", List::class.java)
        //then
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertNotNull(responseEntity.body)
        assertFalse(responseEntity.body!!.isEmpty())
    }

    @Test
    @DisplayName("GET when get my Book list, then returns 200")
    fun whenGetMyBookList_thenReturns200() {

        val token = oAuthService.login("admin@somacode.com", "1234").value
        val headers = HttpHeaders()
        headers.setBearerAuth(token)

        val httpEntity = HttpEntity(null, headers)

        //when
        val responseEntity = restTemplate.exchange("/api/@me/users/1/books?page=0&size=5", HttpMethod.GET, httpEntity, List::class.java)
        //then
        assertEquals(HttpStatus.OK, responseEntity.statusCode)
        assertNotNull(responseEntity.body)
        assertFalse(responseEntity.body!!.isEmpty())
    }
}