package com.breader.hexagonal.domain

import com.breader.hexagonal.isBankingDay
import java.time.LocalDate
import javax.money.MonetaryAmount

class UserAccount(val accountId: UserAccountId, val accountList: List<BankAccount>) {

    fun registerTransfer(transfer: Transfer) : TransferType {
        val fromAccount = findAccountByNumber(transfer.from) ?: throw RegisteringTransferException("Account not found")
        val isEnoughMoney = fromAccount.doesHaveEnoughMoney(transfer.amount)
        if (!isEnoughMoney) {
            throw RegisteringTransferException("Not enough money")
        }

        val toAccount = findAccountByNumber(transfer.to)
        return if (toAccount == null) {
            registerExternalTransfer(fromAccount, transfer.date, transfer.amount)
            TransferType.EXTERNAL
        } else {
            registerInternalTransfer(fromAccount, toAccount, transfer.amount)
            TransferType.INTERNAL
        }
    }

    private fun registerInternalTransfer(from: BankAccount, to: BankAccount, amount: MonetaryAmount) {
        from.balance.subtract(amount)
        to.balance.add(amount)
    }

    private fun registerExternalTransfer(from: BankAccount, date: LocalDate, amount: MonetaryAmount) {
        if (!date.isBankingDay()) {
            throw RegisteringTransferException("External transfer cannot be registered on non banking day")
        }

        from.balance.subtract(amount)
        from.reserved.add(amount)
    }

    private fun findAccountByNumber(accountNumber: String): BankAccount? {
        return accountList.firstOrNull { it.accountNumber == accountNumber }
    }

}

enum class TransferType {
    INTERNAL, EXTERNAL
}
