package domclick.task.exception

import java.lang.RuntimeException

class AccountNotFoundException(accountId: Long): RuntimeException("Account not found. accountId: $accountId")
class NotEnoughFundsException(amount: Double): RuntimeException("Not enough funds. Available amount: $amount")
class PaymentNotFoundException(paymentName: String): RuntimeException("Payment not found. paymentName: $paymentName")