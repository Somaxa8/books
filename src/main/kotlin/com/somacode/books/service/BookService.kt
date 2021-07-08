package com.somacode.books.service

import com.somacode.books.config.exception.NotFoundException
import com.somacode.books.entity.Book
import com.somacode.books.entity.Document
import com.somacode.books.repository.BookRepository
import com.somacode.books.security.SecurityTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class BookService {

    @Autowired lateinit var bookRepository: BookRepository
    @Autowired lateinit var documentService: DocumentService
    @Autowired lateinit var securityTool: SecurityTool

    fun create(
            title: String, author: String?, date: Int?,
            editorial: String?, description: String?, bookFile: MultipartFile
    ): Book {
        val book = Book(
                title = title,
                book = documentService.create(bookFile, Document.Type.DOCUMENT, Book::class.java.simpleName)
        )

        author?.let { book.author = it }
        date?.let { book.date = it }
        editorial?.let { book.editorial = it }
        description?.let { book.description = it }

        return bookRepository.save(book)
    }

    fun update(
            id: Long, title: String?, author: String?, date: Int?,
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
        bookFile?.let { book.book = documentService.create(it, Document.Type.DOCUMENT, Book::class.java.simpleName) }

        return bookRepository.save(book)
    }

    fun findById(id: Long): Book {
        if (!bookRepository.existsById(id)) {
            throw NotFoundException()
        }
        return bookRepository.getOne(id)
    }

    fun deleteById(id: Long) {
        if (!bookRepository.existsByIdAndCreatedBy_Id(id, securityTool.getUserId())) {
            throw NotFoundException()
        }
        bookRepository.deleteById(id)
    }

}