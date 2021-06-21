package com.breader.hexagonal.adapter

import com.breader.hexagonal.domain.UserAccount
import com.breader.hexagonal.domain.UserAccountId
import com.breader.hexagonal.usecase.port.UserRepo

class InMemoryUserRepo : UserRepo {

    private val db: MutableList<UserAccount> = mutableListOf()

    override fun findById(userAccountId: UserAccountId): UserAccount? = db.find { it.accountId == userAccountId }

    override fun findByAccountNumber(accountNumber: String): UserAccount? = db.find { user ->
        user.accountList.map { it.accountNumber }.contains(accountNumber)
    }

    override fun save(userAccount: UserAccount) {
        db.add(userAccount)
    }
}
