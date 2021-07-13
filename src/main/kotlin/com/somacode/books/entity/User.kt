package com.somacode.books.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = [User_.EMAIL])])
class User(
        @Id @GeneratedValue
        var id: Long? = null,
        var email: String? = null,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        var password: String? = null,
        var name: String? = null,
        var lastname: String? = null,
        var active: Boolean? = null,
        @OneToOne
        var avatar: Document? = null,

        @JsonIgnore
        @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
        var authorities: MutableSet<Authority> = mutableSetOf(),

        @ManyToMany
        @JoinTable(
                name = "rel_user_book",
                joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "book_id", referencedColumnName = "id")]
        )
        var favorites: MutableSet<Book> = mutableSetOf(),

): Auditing(), Serializable