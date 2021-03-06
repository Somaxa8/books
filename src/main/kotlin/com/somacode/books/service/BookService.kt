package com.somacode.books.service

import com.somacode.books.config.exception.NotFoundException
import com.somacode.books.entity.Book
import com.somacode.books.entity.Book_
import com.somacode.books.entity.Document
import com.somacode.books.repository.BookRepository
import com.somacode.books.repository.criteria.BookCriteria
import com.somacode.books.security.SecurityTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
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
        var book = Book(
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
            book = findById(response.id!!)
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
            val bookCategory = bookCategoryService.findById(it)
            book.categories.add(bookCategory)
        }

        return bookRepository.save(book)
    }

    fun addFavorite(id: Long, userId:Long) {
        val book = findById(id)
        val user = userService.findById(userId)
        user.bookFavorites.add(book)
    }

    fun removeFavorite(id: Long, userId: Long) {
        val book = findById(id)
        val user = userService.findById(userId)
        user.bookFavorites.remove(book)
    }

    fun findById(id: Long): Book {
        if (!bookRepository.existsById(id)) {
            throw NotFoundException()
        }
        return bookRepository.getOne(id)
    }

    fun findFilterPageable(page: Int, size: Int, search: String?, categoryId: Long?, createdById: Long?, start: LocalDate?, end: LocalDate?, userFavoriteId: Long?): Page<Book> {
        return bookCriteria.findFilterPageable(page, size, search, categoryId, createdById, start, end, userFavoriteId)
    }

    fun delete(id: Long) {
        if (!bookRepository.existsById(id)) {
            throw NotFoundException()
        }
        bookRepository.deleteById(id)
    }

    fun synchronizeBook(
            title: String, author: String, category: String, editorial: String,
            description: String, bookFile: MultipartFile, coverFile: MultipartFile
    ): Book {
        val book = Book(
                title = title,
                author = author,
                editorial = editorial,
                description = description,
                language = languageService.findById(1),
                book = documentService.create(bookFile, Document.Type.DOCUMENT, Book::class.java.simpleName),
                cover = documentService.create(coverFile, Document.Type.IMAGE, Book::class.java.simpleName)
        )

        val response = bookRepository.save(book)

        if (!bookCategoryService.existByTitle(category)) {
            val bookCategory = bookCategoryService.create(category)
            bookCategoryService.relateCategory(bookCategory.id!!, response.id!!)
        } else {
            val bookCategory = bookCategoryService.findByTitle(category)
            bookCategoryService.relateCategory(bookCategory.id!!, response.id!!)
        }

        return response
    }

    fun existsByIdAndCreatedBy_Id(id: Long, userId: Long): Boolean {
        return bookRepository.existsByIdAndCreatedBy_Id(id, userId)
    }

}