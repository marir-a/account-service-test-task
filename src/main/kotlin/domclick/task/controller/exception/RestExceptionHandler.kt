package domclick.task.controller.exception

import domclick.task.exception.AccountNotFoundException
import domclick.task.exception.NotEnoughFundsException
import domclick.task.exception.PaymentNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.IllegalArgumentException

@ControllerAdvice
class RestExceptionHandler: ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [RuntimeException::class])
    fun handle(e: RuntimeException): ResponseEntity<ErrorResponse> =
        when (e) {
            is AccountNotFoundException, is PaymentNotFoundException ->
                ResponseEntity(ErrorResponse(e.localizedMessage), HttpStatus.NOT_FOUND)
            is NotEnoughFundsException ->
                ResponseEntity(ErrorResponse(e.localizedMessage), HttpStatus.UNPROCESSABLE_ENTITY)
            is IllegalArgumentException ->
                ResponseEntity(ErrorResponse(e.localizedMessage), HttpStatus.BAD_REQUEST)
            else -> ResponseEntity(ErrorResponse(e.localizedMessage), HttpStatus.INTERNAL_SERVER_ERROR)
        }

    data class ErrorResponse(
        val message: String
    )
}
