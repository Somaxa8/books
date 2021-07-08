package com.somacode.books.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
class Book(
       @Id @GeneratedValue
       var id: Long? = null,
       var title: String? = null,
       var author: String? = null,
       var editorial: String? = null,
       @Lob var description: String? = null,
       var date: Int? = null,
       @OneToOne
       var book: Document? = null,
       @ManyToOne
       var language: Language? = null,
       @ManyToMany
       @JoinTable(
               name = "rel_book_category",
               joinColumns = [JoinColumn(name = "book_id", referencedColumnName = "id")],
               inverseJoinColumns = [JoinColumn(name = "book_category_id", referencedColumnName = "id")]
       )
       var categories: MutableSet<BookCategory> = mutableSetOf(),
): Auditing()