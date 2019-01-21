package domclick.task.service

import domclick.task.exception.NotEnoughFundsException
import domclick.task.exception.PaymentNotFoundException
import domclick.task.model.Payment
import domclick.task.repository.PaymentDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
class PaymentService(
    private val paymentDao: PaymentDao,
    private val accountService: AccountService
) {

    @Transactional
    fun transfer(payment: Payment): Payment {
        payment.fromAccount ?: throw IllegalArgumentException(withdrawRequired)
        payment.toAccount ?: throw IllegalArgumentException(refillRequired)

        if (payment.amount < 0)
            throw IllegalArgumentException(negativeAmount)

        paymentDao.findByPaymentName(payment.paymentName)
            ?.apply { return this }

        val fromAccount = accountService.get(payment.fromAccount)
        if (fromAccount.amount - payment.amount < 0)
            throw NotEnoughFundsException(fromAccount.amount)

        val toAccount = accountService.get(payment.toAccount)

        accountService.updateAmount(fromAccount, payment.amount * -1)
        accountService.updateAmount(toAccount, payment.amount)

        return paymentDao.save(payment)
    }

    @Transactional
    fun refill(payment: Payment): Payment {
        payment.toAccount ?: throw IllegalArgumentException(refillRequired)

        if (payment.amount < 0)
            throw IllegalArgumentException(negativeAmount)

        paymentDao.findByPaymentName(payment.paymentName)
            ?.apply { return this }

        val toAccount = accountService.get(payment.toAccount)
        accountService.updateAmount(toAccount, payment.amount)

        return paymentDao.save(payment)
    }

    @Transactional
    fun withdraw(payment: Payment): Payment {
        payment.fromAccount ?: throw IllegalArgumentException(withdrawRequired)

        if (payment.amount < 0)
            throw IllegalArgumentException(negativeAmount)

        paymentDao.findByPaymentName(payment.paymentName)
            ?.apply { return this }

        val fromAccount = accountService.get(payment.fromAccount)
        if (fromAccount.amount - payment.amount < 0)
            throw NotEnoughFundsException(fromAccount.amount)

        accountService.updateAmount(fromAccount, payment.amount * -1)

        return paymentDao.save(payment)
    }

    fun getPayment(paymentName: String): Payment =
        paymentDao.findByPaymentName(paymentName) ?: throw PaymentNotFoundException(paymentName)

    companion object {
        private const val withdrawRequired: String = "`withdrawAccountId` is required for this payment type."
        private const val refillRequired: String = "`refillAccountId` is required for this payment type."
        private const val negativeAmount: String = "`amount` must be positive."
    }
}
