package com.breader.hexagonal.domain

import org.javamoney.moneta.Money
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import javax.money.Monetary

internal class UserAccountTest {

    private val userAccountId: UserAccountId = UserAccountId()

    private val PLN = Monetary.getCurrency("PLN")
    private val accountList = listOf(
        BankAccount("PL10 1050 0099 7603 1234 5678 9123", PLN, Money.of(1234, "PLN"), Money.of(0, "PLN")),
        BankAccount("PL10 0700 9312 3456 7867 8900 9976", PLN, Money.of(1234, "PLN"), Money.of(0, "PLN"))
    )

    private val user = UserAccount(userAccountId, accountList)

    private val bankingDate = LocalDate.of(2030, 12, 27)
    private val nonBankingDate = LocalDate.of(2030, 12, 28)

    @Test
    fun transfer_should_not_be_registered_when_user_does_not_have_such_an_account() {
        val t = Transfer(
            "fake account number",
            "PL10 0700 9312 3456 7867 8900 9976",
            Money.of(20, "PLN"),
            bankingDate
        )

        assertThrows(RegisteringTransferException::class.java) { user.registerTransfer(t) }
    }

    @Test
    fun transfer_should_not_be_registered_when_there_is_no_enough_money_on_the_account() {
        val t = Transfer(
            "PL10 1050 0099 7603 1234 5678 9123",
            "PL10 0700 9312 3456 7867 8900 9976",
            Money.of(2000, "PLN"),
            bankingDate
        )

        assertThrows(RegisteringTransferException::class.java) { user.registerTransfer(t) }
    }

    @Test
    fun transfer_should_be_registered_when_there_is_enough_money_on_the_account() {
        val t = Transfer(
            "PL10 1050 0099 7603 1234 5678 9123",
            "PL10 0700 9312 3456 7867 8900 9976",
            Money.of(20, "PLN"),
            bankingDate
        )

        user.registerTransfer(t)
    }

    @Test
    fun transfer_should_be_registered_when_the_date_is_banking_date_and_transfer_is_internal() {
        val t = Transfer(
            "PL10 1050 0099 7603 1234 5678 9123",
            "PL10 0700 9312 3456 7867 8900 9976",
            Money.of(20, "PLN"),
            bankingDate
        )

        user.registerTransfer(t)
    }

    @Test
    fun transfer_should_not_be_registered_when_the_date_is_not_banking_date_and_transfer_is_internal() {
        assertThrows(TransferCreationException::class.java) {
            Transfer(
                "PL10 1050 0099 7603 1234 5678 9123",
                "PL10 0700 9312 3456 7867 8900 9976",
                Money.of(20, "PLN"),
                nonBankingDate
            )
        }
    }

    @Test
    fun transfer_should_be_registered_when_the_date_is_banking_date_and_transfer_is_external() {
        val t = Transfer(
            "PL10 1050 0099 7603 1234 5678 9123",
            "external account",
            Money.of(20, "PLN"),
            bankingDate
        )

        user.registerTransfer(t)
    }

    @Test
    fun transfer_should_not_be_registered_when_the_date_is_not_banking_date_and_transfer_is_external() {
        assertThrows(TransferCreationException::class.java) {
            Transfer(
                "PL10 1050 0099 7603 1234 5678 9123",
                "external account",
                Money.of(20, "PLN"),
                nonBankingDate
            )
        }
    }

}
