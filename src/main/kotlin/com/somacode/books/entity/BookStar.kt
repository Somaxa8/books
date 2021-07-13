package com.somacode.books.entity

import javax.persistence.*
import javax.validation.constraints.Size

@Entity
class BookStar(
        @Id @GeneratedValue
        var id: Long? = null,
        @ManyToOne
        var user: User? = null,
        @ManyToOne
        var book: Book? = null,
        @Size(max = 5)
        var count: Int? = null
)