package domclick.task.controller

import domclick.task.dto.AccountResponse
import domclick.task.model.Account
import domclick.task.service.AccountService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService
) {

    @PutMapping("/{accountName}")
    fun create(
        @PathVariable("accountName") accountName: String
    ): AccountResponse {
        log.info("createAccount. accountName: $accountName")
        return accountService.create(accountName).toAccountResponse()
            .also { log.info("accountResponse: $it") }
    }

    @GetMapping("/{accountId}")
    fun get(
        @PathVariable("accountId") accountId: Long
    ): AccountResponse {
        log.info("getAccount. accountId: $accountId")
        return accountService.get(accountId).toAccountResponse()
            .also { log.info("accountResponse: $it") }
    }

    private fun Account.toAccountResponse(): AccountResponse =
        AccountResponse(
            accountId = this.id,
            accountName = this.accountName,
            amount = this.amount
        )

    companion object {
        private val log: Logger = LoggerFactory.getLogger(AccountController::class.java)
    }
}
