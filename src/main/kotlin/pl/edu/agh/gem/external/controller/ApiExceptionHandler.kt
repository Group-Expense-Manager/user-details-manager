package pl.edu.agh.gem.external.controller

import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.edu.agh.gem.error.SimpleErrorsHolder
import pl.edu.agh.gem.error.handleError
import pl.edu.agh.gem.exception.UserWithoutGroupAccessException
import pl.edu.agh.gem.internal.service.MissingUserDetailsException

@ControllerAdvice
@Order(LOWEST_PRECEDENCE)
class ApiExceptionHandler {

    @ExceptionHandler(MissingUserDetailsException::class)
    fun handleMissingUserDetailsException(exception: MissingUserDetailsException): ResponseEntity<SimpleErrorsHolder> {
        return ResponseEntity(handleError(exception), NOT_FOUND)
    }

    @ExceptionHandler(UserWithoutGroupAccessException::class)
    fun handleUserWithoutGroupAccessException(
        exception: UserWithoutGroupAccessException,
    ): ResponseEntity<SimpleErrorsHolder> {
        return ResponseEntity(handleError(exception), FORBIDDEN)
    }

    @ExceptionHandler(UserNotGroupMemberException::class)
    fun handleUserNotGroupMemberException(
        exception: UserNotGroupMemberException,
    ): ResponseEntity<SimpleErrorsHolder> {
        return ResponseEntity(handleError(exception), FORBIDDEN)
    }
}
