package com.somacode.books.repository

import com.somacode.books.entity.BookCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookCategoryRepository: JpaRepository<BookCategory, Long> {

    fun existsByIdAndCreatedBy_Id(id: Long, userId: Long): Boolean

    fun existsByTitle(title: String): Boolean

    fun findByTitle(title: String): BookCategory

}