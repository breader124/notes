package com.breader.hexagonal.usecase

import com.breader.hexagonal.domain.Transfer
import com.breader.hexagonal.domain.TransferCreationException
import com.breader.hexagonal.domain.TransferType
import com.breader.hexagonal.usecase.port.TransferApi
import com.breader.hexagonal.usecase.port.TransferRepo
import com.breader.hexagonal.usecase.port.UserRepo
import org.javamoney.moneta.Money
import org.springframework.lang.NonNull
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import javax.money.Monetary

// Creating transfer use case implementation, please note, that this class mostly checks existence and integrates
// business logic results with repositories, all the magic happens in classes belonging to pure domain
class CreatingTransfer(
    private val userRepo: UserRepo,
    private val transferRepo: TransferRepo,
    private val transferApi: TransferApi
) {

    fun createTransfer(request: CreateTransferRequest) {
        val debtor = userRepo.findByAccountNumber(request.from) ?: throw TransferCreationException("No such debtor account")
        val doesCreditorAccountExists = transferApi.isAccountNumberCorrect(request.to)
        if (!doesCreditorAccountExists) {
            throw TransferCreationException("No such creditor account")
        }

        val date = LocalDate.from(request.timestamp)
        val amount = Money.of(request.amount, request.currency)
        val newTransfer = Transfer(request.from, request.to, amount, date)
        transferRepo.save(newTransfer)

        val type = debtor.registerTransfer(newTransfer)
        if (type == TransferType.EXTERNAL) {
            transferApi.notifyExternalBankAboutTransfer(request.to, date, amount)
        }
        userRepo.save(debtor)
    }

}

data class CreateTransferRequest(
    @NonNull val from: String,
    @NonNull val to: String,
    @NonNull val timestamp: Instant,
    @NonNull val amount: BigDecimal,
    @NonNull val currency: String
)
