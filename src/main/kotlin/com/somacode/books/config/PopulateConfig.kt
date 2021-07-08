package com.somacode.books.config

import com.somacode.books.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PopulateConfig {

    @Autowired lateinit var authorityService: AuthorityService
    @Autowired lateinit var userService: UserService
    @Autowired lateinit var bookCategoryService: BookCategoryService
    @Autowired lateinit var languageService: LanguageService

    fun init() {
        authorityService.init()
        userService.init()
        bookCategoryService.init()
        languageService.init()
    }

}