package com.somacode.books.repository.criteria

import com.somacode.books.entity.*
import com.somacode.books.service.tool.CriteriaTool
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Join
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate

@Repository
class BookCriteria {

    @PersistenceContext
    lateinit var entityManager: EntityManager


    fun findFilterPageable(page: Int, size: Int, search: String?, categoryId: Long?, createdById: Long?, start: LocalDate?, end: LocalDate?, userFavoriteId: Long?): Page<Book> {
        val cb = entityManager.criteriaBuilder
        val q = cb.createQuery(Book::class.java)
        val book = q.from(Book::class.java)

        val order = book.get(Book_.id)

        val predicates: MutableList<Predicate> = mutableListOf()

        createdById?.let {
            predicates.add(cb.equal(book.join(Book_.createdBy).get(User_.id), it))
        }

        categoryId?.let {
            predicates.add(cb.equal(book.join(Book_.categories).get(BookCategory_.id), it))
        }

        userFavoriteId?.let {
            predicates.add(cb.equal(book.join(Book_.userFavorites).get(User_.id), it))
        }

        // search between dates
        if (start != null && end != null) {
            predicates.add(
                    cb.or(
                            cb.between(book.get(Book_.createdAt), start, end),
                            cb.between(book.get(Book_.createdAt), end, start)
                    )
            )
        }

        if (start != null && end == null) {
            predicates.add(cb.equal(book.get(Book_.createdAt), start))
        }

        if (!search.isNullOrBlank()) {
            val word = search.trim().toLowerCase()
            val like = "%$word%"
            predicates.add(
                    cb.or(
                            cb.like(cb.lower(book.get(Book_.title)), like),
                            cb.like(cb.lower(book.get(Book_.author)), like),
                            cb.like(cb.lower(book.get(Book_.editorial)), like)
                    )
            )
        }

        q.select(book).where(
                *predicates.toTypedArray(),
        ).orderBy(cb.desc(order))

        return CriteriaTool.page(entityManager, q, page, size)
    }

}