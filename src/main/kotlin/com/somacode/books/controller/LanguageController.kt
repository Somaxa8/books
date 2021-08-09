package com.somacode.books.controller

import com.somacode.books.entity.Language
import com.somacode.books.service.LanguageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class LanguageController {

    @Autowired lateinit var languageService: LanguageService

    @PostMapping("/api/languages")
    fun postLanguage(@RequestParam title: String): ResponseEntity<Language> {
        return ResponseEntity.status(HttpStatus.CREATED).body(languageService.create(title))
    }

    @GetMapping("/public/languages")
    fun getLanguages(): ResponseEntity<List<Language>> {
        return ResponseEntity.status(HttpStatus.OK).body(languageService.findAll())
    }

    @DeleteMapping("/api/languages/{id}")
    fun deleteLanguage(@PathVariable id: Long): ResponseEntity<Void> {
        languageService.delete(id)
        return ResponseEntity.status(HttpStatus.OK).body(null)
    }

}