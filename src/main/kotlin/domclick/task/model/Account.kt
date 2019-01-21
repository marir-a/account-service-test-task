package domclick.task.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Account (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountId")
    val id: Long = 0,

    @Column(name = "accountName", unique = true)
    val accountName: String,

    @Column(name = "amount")
    val amount: Double = 0.0
) {
    override fun toString(): String {
        return "Account(id=$id, accountName='$accountName', amount=$amount)"
    }
}
