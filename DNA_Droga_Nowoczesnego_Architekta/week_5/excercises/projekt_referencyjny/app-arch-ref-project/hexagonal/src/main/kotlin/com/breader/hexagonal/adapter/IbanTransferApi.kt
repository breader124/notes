package com.breader.hexagonal.adapter

import com.breader.hexagonal.usecase.port.TransferApi
import java.time.LocalDate
import javax.money.MonetaryAmount

class IbanTransferApi : TransferApi {

    override fun isAccountNumberCorrect(accountNumber: String): Boolean {
        TODO("Integration with external account validation service")
    }

    override fun notifyExternalBankAboutTransfer(
        externalAccountNum: String,
        date: LocalDate,
        monetaryAmount: MonetaryAmount
    ) {
        TODO("Not yet implemented")
    }
}
