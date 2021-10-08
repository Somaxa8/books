package com.somacode.books.service

import com.somacode.books.config.exception.NotFoundException
import com.somacode.books.entity.Authority
import com.somacode.books.entity.BookCategory
import com.somacode.books.repository.BookCategoryRepository
import com.somacode.books.security.SecurityTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookCategoryService {

    @Autowired lateinit var bookCategoryRepository: BookCategoryRepository
    @Autowired lateinit var bookService: BookService
    @Autowired lateinit var securityTool: SecurityTool

    fun init() {
        if (bookCategoryRepository.count() == 0L) {
            create("Filosofía")
            create("Teología")
            create("Escolástica")
            create("Historia")
            create("Narrativa")
            create("Ciencia")
            create("Matemática")
            create("Biográfia")
            create("Sagrado")
            create("Cristianismo")
            create("Judaísmo")
            create("Oriental")
            create("Islam")
            create("Humor")
            create("Arte")
            create("Cómic")
            create("Manga")
            create("Novela")
            create("Programación")
        }
    }

    fun create(title: String): BookCategory {
        if (title.isNullOrBlank()) {
            throw IllegalArgumentException()
        }
        return bookCategoryRepository.save(BookCategory(title = title))
    }

    fun update(id: Long, request: BookCategory): BookCategory {
        if (!bookCategoryRepository.existsByIdAndCreatedBy_Id(id, securityTool.getUserId())) {
            throw NotFoundException()
        }

        val bookCategory = bookCategoryRepository.getOne(id)

        if (request.title.isNullOrBlank()) {
            throw IllegalArgumentException()
        }

        bookCategory.title = request.title
        return bookCategoryRepository.save(bookCategory)
    }

    fun relateCategory(id: Long, bookId: Long) {
        val bookCategory = findById(id)
        val book = bookService.findById(bookId)
        book.categories.add(bookCategory)
    }

    fun unrelateCategory(id: Long, bookId: Long) {
        val bookCategory = findById(id)
        val book = bookService.findById(bookId)
        book.categories.remove(bookCategory)
    }

    fun findAll(): List<BookCategory> {
        return bookCategoryRepository.findAll()
    }

    fun findById(id: Long): BookCategory {
        if (!bookCategoryRepository.existsById(id)) {
            throw NotFoundException()
        }
        return bookCategoryRepository.getOne(id)
    }

    fun delete(id: Long) {
        if (!bookCategoryRepository.existsById(id)) {
            throw NotFoundException()
        }
        bookCategoryRepository.deleteById(id)
    }

    fun existByTitle(title: String): Boolean {
        return bookCategoryRepository.existsByTitle(title)
    }

    fun findByTitle(title: String): BookCategory {
        return bookCategoryRepository.findByTitle(title)
    }
}