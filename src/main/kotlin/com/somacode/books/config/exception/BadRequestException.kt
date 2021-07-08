package com.somacode.books.config.exception

class BadRequestException: RuntimeException {
    constructor(message: String?): super(message)
    constructor(): super("BadRequest")
}