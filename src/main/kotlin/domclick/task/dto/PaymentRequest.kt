package domclick.task.dto

import domclick.task.model.PaymentType

data class PaymentRequest(
    var paymentName: String = "",
    var paymentType: PaymentType = PaymentType.REFILL,
    var withdrawAccountId: Long? = null,
    var refillAccountId: Long? = null,
    var amount: Double = 0.0
)
