package com.breader.hexagonal.domain

import com.breader.hexagonal.isBankingDay
import org.javamoney.moneta.Money
import java.math.BigDecimal
import java.time.LocalDate
import javax.money.Monetary
import javax.money.MonetaryAmount

class Transfer(val from: String, val to: String, val amount: MonetaryAmount, val date: LocalDate) {

    val id: TransferId = TransferId()

    init {
        if (date.isBefore(LocalDate.now())) {
            throw TransferCreationException("Date can't be in the past")
        }

        if (!date.isBankingDay()) {
            throw TransferCreationException("Not a banking day")
        }

        if (doesAmountExceedBankLimit()) {
            throw TransferCreationException("Amount exceeds bank limit, please contact your bank")
        }
    }

    private fun doesAmountExceedBankLimit(): Boolean {
        val limit = Money.of(BigDecimal.valueOf(5000000), Monetary.getCurrency("PLN"))
        return amount.isGreaterThan(limit)
    }

}
