package com.somacode.books.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class BookCategory(
       @Id @GeneratedValue
       var id: Long? = null,
       var title: String? = null,
       @JsonIgnore
       @OneToMany(mappedBy = "bookCategory")
       val books: List<Book> = listOf()
): Auditing()