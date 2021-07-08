package com.somacode.books.service

import com.somacode.books.entity.Language
import com.somacode.books.repository.LanguageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LanguageService {

    @Autowired lateinit var languageRepository: LanguageRepository

    fun init() {
        if (languageRepository.count() == 0L) {
            create("Español")
            create("Ingles")
            create("Portugués")
            create("Latín")
        }
    }

    fun create(title: String): Language {
        return languageRepository.save(Language(title = title))
    }

}