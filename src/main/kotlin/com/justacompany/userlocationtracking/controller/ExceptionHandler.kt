package com.justacompany.userlocationtracking.controller

import com.justacompany.userlocationtracking.exception.UserAlreadyRegisteredException
import com.justacompany.userlocationtracking.periphery.ErrorBody
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(UserAlreadyRegisteredException::class)
    fun handleUserAlreadyRegisteredException(
        ex : UserAlreadyRegisteredException,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.warn("UserAlreadyRegisteredException occurred : ${ex.message}", ex)
        return handleExceptionInternal(ex, makeErrorBody("UserAlreadyRegisteredException"), HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    private fun makeErrorBody(name: String): ErrorBody {
        return ErrorBody(errorName = name)
    }

}
