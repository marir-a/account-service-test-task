package domclick.task.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Payment (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentId")
    val paymentId: Long = 0,

    @Column(name = "paymentName", unique = true)
    val paymentName: String,

    @Column(name = "fromAccountId")
    val fromAccount: Long?,

    @Column(name = "toAccountId")
    val toAccount: Long?,

    @Column(name = "amount")
    val amount: Double
) {
    override fun toString(): String {
        return "Payment(paymentId=$paymentId, fromAccount=$fromAccount, toAccount=$toAccount, amount=$amount, paymentName='$paymentName')"
    }
}
