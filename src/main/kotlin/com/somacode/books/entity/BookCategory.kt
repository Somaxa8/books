package com.somacode.books.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class BookCategory(
       @Id @GeneratedValue
       var id: Long? = null,
       var title: String? = null,
       @JsonIgnore
       @ManyToMany(mappedBy = "categories")
       val books: MutableSet<Book> = mutableSetOf()
): Auditing()