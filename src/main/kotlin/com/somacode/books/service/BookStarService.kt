package com.somacode.books.service

import com.somacode.books.config.exception.NotFoundException
import com.somacode.books.entity.BookStar
import com.somacode.books.repository.BookStarRepository
import com.somacode.books.security.SecurityTool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookStarService {

    @Autowired lateinit var bookStarRepository: BookStarRepository
    @Autowired lateinit var bookService: BookService
    @Autowired lateinit var userService: UserService
    @Autowired lateinit var securityTool: SecurityTool

    fun bookStar(userId: Long?, bookId: Long, count: Int): BookStar {
        lateinit var bookStar: BookStar
        userId?.let {
            if (!existsByUserIdAndBookId(it, bookId)) {
                throw NotFoundException()
            }
            bookStar = findByUserIdAndBookId(it, bookId)
            bookStar.count = count
        } ?: run {
            bookStar = BookStar(
                    book = bookService.findById(bookId),
                    user = userService.findById(securityTool.getUserId()),
                    count = count
            )
        }
        return bookStarRepository.save(bookStar)
    }

    fun findByUserIdAndBookId(userId: Long, bookId: Long): BookStar {
        return bookStarRepository.findByUser_IdAndBook_Id(userId, bookId)
    }

    fun existsByUserIdAndBookId(userId: Long, bookId: Long): Boolean {
        return bookStarRepository.existsByUser_IdAndBook_Id(userId, bookId)
    }
}