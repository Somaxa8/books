package com.somacode.books.repository

import com.somacode.books.entity.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository: JpaRepository<Book, Long> {

    fun existsByIdAndCreatedBy_Id(id: Long, userId: Long): Boolean

    fun findAllByUserFavorites_Id(userId: Long, pageable: Pageable): Page<Book>

}