package domclick.task.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PaymentResponse(
    val paymentId: Long,
    val paymentName: String,
    val withdrawAccountId: Long?,
    val refillAccountId: Long?,
    val amount: Double
)
