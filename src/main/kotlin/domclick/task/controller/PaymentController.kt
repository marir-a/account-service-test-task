package domclick.task.controller

import domclick.task.dto.PaymentRequest
import domclick.task.dto.PaymentResponse
import domclick.task.model.Payment
import domclick.task.model.PaymentType.REFILL
import domclick.task.model.PaymentType.TRANSFER
import domclick.task.model.PaymentType.WITHDRAW
import domclick.task.service.PaymentService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payment/{paymentName}")
class PaymentController(
    private val paymentService: PaymentService
) {

    @PutMapping
    fun create(
        @PathVariable("paymentName") paymentName: String,
        request: PaymentRequest
    ): PaymentResponse {
        log.info("createPayment. paymentName: $paymentName, request: $request")
        val payment = Payment(
            fromAccount = request.withdrawAccountId,
            toAccount = request.refillAccountId,
            amount = request.amount,
            paymentName = request.paymentName
        )
        return when (request.paymentType) {
            WITHDRAW -> paymentService.withdraw(payment)
            REFILL -> paymentService.refill(payment)
            TRANSFER -> paymentService.transfer(payment)
        }
            .toPaymentResponse()
            .also { log.info("paymentResponse: $it") }
    }

    @GetMapping
    fun get(
        @PathVariable("paymentName") paymentName: String
    ): PaymentResponse {
        log.info("getPayment. paymentName: $paymentName")
        return paymentService.getPayment(paymentName)
            .toPaymentResponse()
            .also { log.info("paymentResponse: $it") }
    }

    private fun Payment.toPaymentResponse(): PaymentResponse =
        PaymentResponse(
            paymentId = this.paymentId,
            paymentName = this.paymentName,
            withdrawAccountId = this.fromAccount,
            refillAccountId = this.toAccount,
            amount = this.amount
        )

    companion object {
        private val log: Logger = LoggerFactory.getLogger(PaymentController::class.java)
    }
}
