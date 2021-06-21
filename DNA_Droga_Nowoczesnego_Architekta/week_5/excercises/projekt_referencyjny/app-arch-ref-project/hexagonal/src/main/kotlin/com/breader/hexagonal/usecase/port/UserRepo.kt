package com.breader.hexagonal.usecase.port

import com.breader.hexagonal.domain.UserAccount
import com.breader.hexagonal.domain.UserAccountId

interface UserRepo {
    fun findById(userAccountId: UserAccountId): UserAccount?
    fun findByAccountNumber(accountNumber: String): UserAccount?
    fun save(userAccount: UserAccount)
}
