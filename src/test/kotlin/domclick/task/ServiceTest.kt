package domclick.task

import domclick.task.exception.AccountNotFoundException
import domclick.task.exception.NotEnoughFundsException
import domclick.task.exception.PaymentNotFoundException
import domclick.task.model.Payment
import domclick.task.service.AccountService
import domclick.task.service.PaymentService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner::class)
class ServiceTest {

    @Autowired
    private lateinit var paymentService: PaymentService

    @Autowired
    private lateinit var accountService: AccountService

    @Test
    fun createAndGetAccountTest() {
        val acc1Create = accountService.create("accountTest1")
        val acc2Create = accountService.create("accountTest2")

        val acc1Get = accountService.get(acc1Create.id)
        val acc2Get = accountService.get(acc2Create.id)

        assertEquals(acc1Create.toString(), acc1Get.toString())
        assertEquals(acc2Create.toString(), acc2Get.toString())
    }

    @Test
    fun updateAmountTest() {
        val account = accountService.create("accountTest")
        val account2 = accountService.updateAmount(account, 500.0)

        assertEquals(account.amount + 500.0, account2.amount)

        val account3 = accountService.updateAmount(account2, -300.0)

        assertEquals(account2.amount - 300.0, account3.amount)
    }

    @Test
    fun withdrawTest() {
        val large = accountService.create("largeAmount")
            .let { accountService.updateAmount(it, 5000.0) }

        assertEquals(5000.0, large.amount)

        val withdrawPayment = Payment(
            paymentName = "withdraw500",
            fromAccount = large.id,
            toAccount = null,
            amount = 500.0
        )
        val withdrawResult = paymentService.withdraw(withdrawPayment)

        assertNotNull(withdrawResult.fromAccount)
        assertEquals(500.0, withdrawResult.amount)

        val updatedLarge = accountService.get(large.id)
        assertEquals(5000.0 - 500.0, updatedLarge.amount)
    }

    @Test(expected = NotEnoughFundsException::class)
    fun withdrawZeroFunds() {
        val zero = accountService.create("zeroAmount")

        assertEquals(0.0, zero.amount)

        val withdrawPayment = Payment(
            paymentName = "withdraw50",
            fromAccount = zero.id,
            toAccount = null,
            amount = 50.0
        )
        paymentService.withdraw(withdrawPayment)
    }

    @Test(expected = AccountNotFoundException::class)
    fun withdrawAccountNotFound() {
        val withdrawPayment = Payment(
            paymentName = "withdraw10",
            fromAccount = 5000,
            toAccount = null,
            amount = 10.0
        )
        paymentService.withdraw(withdrawPayment)
    }

    @Test(expected = IllegalArgumentException::class)
    fun transferNegativeAmount() {
        val medium = accountService.create("medium")
            .let { accountService.updateAmount(it, 100.0) }
        val zero = accountService.create("zero")

        val transferPayment = Payment(
            paymentName = "transferNegative",
            fromAccount = medium.id,
            toAccount = zero.id,
            amount = -10.0
        )
        paymentService.transfer(transferPayment)
    }

    @Test
    fun transfer() {
        val from = accountService.create("accFrom")
            .let { accountService.updateAmount(it, 100.0) }
        val to = accountService.create("accTo")
            .let { accountService.updateAmount(it, 100.0) }

        val transferPayment = Payment(
            paymentName = "transfer50",
            fromAccount = from.id,
            toAccount = to.id,
            amount = 50.0
        )
        paymentService.transfer(transferPayment)
        val fromUpdated = accountService.get(from.id)
        val toUpdated = accountService.get(to.id)

        assertEquals(50.0, fromUpdated.amount)
        assertEquals(150.0, toUpdated.amount)
    }

    @Test
    fun refill() {
        val account = accountService.create("refillAccount")
        val refillPayment = Payment(
            paymentName = "refillPayment",
            fromAccount = null,
            toAccount = account.id,
            amount = 5000.0
        )
        val accountUpdated = paymentService.refill(refillPayment)

        assertEquals(5000.0, accountUpdated.amount)
    }

    @Test
    fun getPayment() {
        val account = accountService.create("testAccount")
        val refillPayment = Payment(
            paymentName = "testRefillPayment",
            fromAccount = null,
            toAccount = account.id,
            amount = 500.0
        )
        paymentService.refill(refillPayment)
        val payment = paymentService.getPayment("testRefillPayment")

        assertEquals(refillPayment.toString(), payment.toString())
    }

    @Test(expected = PaymentNotFoundException::class)
    fun paymentNotFound() {
        paymentService.getPayment("notExists")
    }
}
