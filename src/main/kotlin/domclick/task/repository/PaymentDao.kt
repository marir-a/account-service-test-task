package domclick.task.repository

import domclick.task.model.Payment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentDao: CrudRepository<Payment, Long> {
    fun findByPaymentName(paymentName: String): Payment?
}
