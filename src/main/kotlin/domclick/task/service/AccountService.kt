package domclick.task.service

import domclick.task.exception.AccountNotFoundException
import domclick.task.model.Account
import domclick.task.repository.AccountDao
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val accountDao: AccountDao
) {

    @Transactional
    fun create(accountName: String): Account =
        accountDao.findByAccountName(accountName)
            ?: accountDao.save(Account(accountName = accountName))

    @Transactional
    fun get(accountId: Long): Account =
        accountDao.findOne(accountId) ?: throw AccountNotFoundException(accountId)

    @Transactional(propagation = Propagation.REQUIRED)
    fun updateAmount(account: Account, deltaAmount: Double): Account =
        accountDao.save(getUpdatedAccount(account, deltaAmount))

    private fun getUpdatedAccount(account: Account, deltaAmount: Double): Account =
        Account(id = account.id, accountName = account.accountName, amount = account.amount + deltaAmount)
}
