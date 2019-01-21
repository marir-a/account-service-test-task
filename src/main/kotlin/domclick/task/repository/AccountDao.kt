package domclick.task.repository

import domclick.task.model.Account
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AccountDao: CrudRepository<Account, Long> {
    fun findByAccountName(accountName: String): Account?
}
