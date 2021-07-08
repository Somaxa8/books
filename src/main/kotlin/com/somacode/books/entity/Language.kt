package com.somacode.books.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Language(
       @Id @GeneratedValue
       var id: Long? = null,
       var title: String? = null
)