package com.somacode.books.repository

import com.somacode.books.entity.BookStar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookStarRepository: JpaRepository<BookStar, Long> {

    fun findByUser_IdAndBook_Id(userId: Long, bookId: Long): BookStar

    fun existsByUser_IdAndBook_Id(userId: Long, bookId: Long): Boolean

}