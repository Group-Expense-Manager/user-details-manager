package pl.edu.agh.gem.external.controller

import org.springframework.core.Ordered.LOWEST_PRECEDENCE
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.edu.agh.gem.error.SimpleErrorsHolder
import pl.edu.agh.gem.error.handleError
import pl.edu.agh.gem.error.handleNotValidException
import pl.edu.agh.gem.exception.UserWithoutGroupAccessException
import pl.edu.agh.gem.internal.client.AttachmentStoreClientException
import pl.edu.agh.gem.internal.client.GroupManagerClientException
import pl.edu.agh.gem.internal.client.RetryableAttachmentStoreClientException
import pl.edu.agh.gem.internal.client.RetryableGroupManagerClientException
import pl.edu.agh.gem.internal.service.MissingUserDetailsException

@ControllerAdvice
@Order(LOWEST_PRECEDENCE)
class ApiExceptionHandler {

    @ExceptionHandler(MissingUserDetailsException::class)
    fun handleMissingUserDetailsException(exception: MissingUserDetailsException): ResponseEntity<SimpleErrorsHolder> {
        return ResponseEntity(handleError(exception), INTERNAL_SERVER_ERROR)
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        exception: MethodArgumentNotValidException,
    ): ResponseEntity<SimpleErrorsHolder> {
        return ResponseEntity(handleNotValidException(exception), BAD_REQUEST)
    }

    @ExceptionHandler(RetryableAttachmentStoreClientException::class)
    fun handleRetryableAttachmentStoreClientException(
        exception: RetryableAttachmentStoreClientException,
    ): ResponseEntity<SimpleErrorsHolder> {
        return ResponseEntity(handleError(exception), INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(AttachmentStoreClientException::class)
    fun handleUserDetailsManagerClientException(
        exception: AttachmentStoreClientException,
    ): ResponseEntity<SimpleErrorsHolder> {
        return ResponseEntity(handleError(exception), INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(RetryableGroupManagerClientException::class)
    fun handleRetryableGroupManagerClientException(
        exception: RetryableGroupManagerClientException,
    ): ResponseEntity<SimpleErrorsHolder> {
        return ResponseEntity(handleError(exception), INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(GroupManagerClientException::class)
    fun handleGroupManagerClientException(
        exception: GroupManagerClientException,
    ): ResponseEntity<SimpleErrorsHolder> {
        return ResponseEntity(handleError(exception), INTERNAL_SERVER_ERROR)
    }
}
