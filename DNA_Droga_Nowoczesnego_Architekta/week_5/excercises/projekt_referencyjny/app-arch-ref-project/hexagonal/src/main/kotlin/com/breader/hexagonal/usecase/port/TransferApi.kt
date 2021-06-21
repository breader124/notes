package com.breader.hexagonal.usecase.port

import java.time.LocalDate
import javax.money.MonetaryAmount

interface TransferApi {

    fun isAccountNumberCorrect(accountNumber: String): Boolean
    fun notifyExternalBankAboutTransfer(externalAccountNum: String, date: LocalDate, monetaryAmount: MonetaryAmount)

}
