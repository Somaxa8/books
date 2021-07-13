package com.somacode.books.repository.criteria

import com.somacode.books.entity.Authority
import com.somacode.books.entity.Book
import com.somacode.books.service.tool.CriteriaTool
import org.springframework.data.domain.Page
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.Path
import javax.persistence.criteria.Predicate

@Repository
class BookCriteria {

    @PersistenceContext
    lateinit var entityManager: EntityManager


//    fun findFilterPageable(page: Int, size: Int, search: String?): Page<Book> {
//        val cb = entityManager.criteriaBuilder
//        val q = cb.createQuery(Book::class.java)
//        val book = q.from(Book::class.java)
//
//        val order: Path<Set<String>> = book.get(Book_.ID)
//
//        val predicates: MutableList<Predicate> = mutableListOf()
//
//        if (!search.isNullOrBlank()) {
//            val word = search.trim().toLowerCase()
//            val like = "%$word%"
//            predicates.add(
//                    cb.or(
//                            cb.like(cb.lower(order.get(Book_.title)), like),
//                            cb.like(cb.lower(order.get(Book_.author)), like),
//                            cb.like(cb.lower(order.get(Book_.editorial)), like)
//                    )
//            )
//        }
//
//        q.select(book).where(
//                *predicates.toTypedArray(),
//        ).orderBy(cb.desc(order))
//
//        return CriteriaTool.page(entityManager, q, page, size)
//    }

}