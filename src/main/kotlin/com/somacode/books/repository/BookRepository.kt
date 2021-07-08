package com.somacode.books.repository

import com.somacode.books.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<Book, Long> {

    fun existsByIdAndCreatedBy_Id(id: Long, userId: Long): Boolean

}