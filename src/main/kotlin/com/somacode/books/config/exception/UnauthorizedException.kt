package com.somacode.books.config.exception

class UnauthorizedException: RuntimeException {
    constructor(message: String?): super(message)
    constructor(): super("BadRequest")
}