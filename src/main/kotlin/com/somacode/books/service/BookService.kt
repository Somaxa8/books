package com.somacode.books.service

import com.somacode.books.config.exception.NotFoundException
import com.somacode.books.entity.Book
import com.somacode.books.entity.Document
import com.somacode.books.repository.BookRepository
import com.somacode.books.repository.criteria.BookCriteria
import com.somacode.books.security.SecurityTool
import com.somacode.books.service.BookCategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@Service
@Transactional
class BookService {

    @Autowired lateinit var bookRepository: BookRepository
    @Autowired lateinit var documentService: DocumentService
    @Autowired lateinit var userService: UserService
    @Autowired lateinit var securityTool: SecurityTool
    @Autowired lateinit var languageService: LanguageService
    @Autowired lateinit var bookCriteria: BookCriteria
    @Autowired lateinit var bookCategoryService: BookCategoryService

    fun create(
            title: String, author: String?, date: LocalDate?, languageId: Long, categoryIds: List<Long>,
            editorial: String?, description: String?, bookFile: MultipartFile
    ): Book {
        val book = Book(
                title = title,
                language = languageService.findById(languageId),
                book = documentService.create(bookFile, Document.Type.DOCUMENT, Book::class.java.simpleName)
        )

        author?.let { book.author = it }
        date?.let { book.date = it }
        editorial?.let { book.editorial = it }
        description?.let { book.description = it }

        val response = bookRepository.save(book)

        categoryIds.forEach {
            val book = findById(response.id!!)
            val bookCategory = bookCategoryService.findById(it)
            book.categories.add(bookCategory)
        }

        return response
    }

    fun update(
            id: Long, title: String?, author: String?, date: LocalDate?, languageId: Long?, categoryIds: List<Long>,
            editorial: String?, description: String?, bookFile: MultipartFile?
    ): Book {
        if (!bookRepository.existsByIdAndCreatedBy_Id(id, securityTool.getUserId())) {
            throw NotFoundException()
        }

        val book = bookRepository.getOne(id)

        title?.let { book.title = it }
        author?.let { book.author = it }
        date?.let { book.date = it }
        editorial?.let { book.editorial = it }
        description?.let { book.description = it }
        languageId?.let { book.language = languageService.findById(it) }
        bookFile?.let { book.book = documentService.create(it, Document.Type.DOCUMENT, Book::class.java.simpleName) }
        book.categories = mutableSetOf()

        categoryIds.forEach {
            val book = findById(id)
            val bookCategory = bookCategoryService.findById(it)
            book.categories.add(bookCategory)
        }

        return bookRepository.save(book)
    }

    fun addFavorite(id: Long, userId:Long) {
        val book = findById(id)
        val user = userService.findById(userId)
        user.favorites.add(book)
    }

    fun removeFavorite(id: Long, userId: Long) {
        val book = findById(id)
        val user = userService.findById(userId)
        user.favorites.remove(book)
    }

    fun findById(id: Long): Book {
        if (!bookRepository.existsById(id)) {
            throw NotFoundException()
        }
        return bookRepository.getOne(id)
    }

    fun findFilterPageable(page: Int, size: Int, search: String?): Page<Book> {
        return bookCriteria.findFilterPageable(page, size, search)
    }

    fun delete(id: Long) {
        if (!bookRepository.existsByIdAndCreatedBy_Id(id, securityTool.getUserId())) {
            throw NotFoundException()
        }
        bookRepository.deleteById(id)
    }

}