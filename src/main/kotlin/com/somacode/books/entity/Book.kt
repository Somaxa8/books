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
       var date: LocalDate? = null,
       @OneToOne
       var book: Document? = null,
): Auditing()