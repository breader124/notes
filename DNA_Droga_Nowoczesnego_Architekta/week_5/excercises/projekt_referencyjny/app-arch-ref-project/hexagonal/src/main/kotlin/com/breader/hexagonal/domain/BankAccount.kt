package com.breader.hexagonal.domain

import javax.money.CurrencyUnit
import javax.money.MonetaryAmount

data class BankAccount(
    val accountNumber: String,
    val accountCurrency: CurrencyUnit,
    val balance: MonetaryAmount,
    val reserved: MonetaryAmount
) {
    fun doesHaveEnoughMoney(amount: MonetaryAmount) = balance.isGreaterThan(amount)
}
