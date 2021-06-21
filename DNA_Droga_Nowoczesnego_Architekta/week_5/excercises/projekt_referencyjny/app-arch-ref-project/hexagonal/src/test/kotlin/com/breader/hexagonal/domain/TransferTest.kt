package com.breader.hexagonal.domain

import org.javamoney.moneta.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import java.time.LocalDate

internal class TransferTest {

    private val bankingDate = LocalDate.of(2030, 12, 27)
    private val nonBankingDate = LocalDate.of(2030, 12, 28)

    @Test
    fun transfer_should_be_created_when_date_is_in_future_and_date_is_banking_day_and_amount_does_not_exceed_limit() {
        Transfer(
            "some account",
            "someAnotherAccount",
            Money.of(20, "PLN"),
            bankingDate
        )
    }

    @Test
    fun transfer_should_not_be_created_when_date_is_in_the_past() {
        assertThrows(TransferCreationException::class.java) {
            Transfer(
                "some account",
                "someAnotherAccount",
                Money.of(20, "PLN"),
                LocalDate.now().minusDays(1)
            )
        }
    }

    @Test
    fun transfer_should_not_be_created_when_date_is_not_a_banking_day() {
        assertThrows(TransferCreationException::class.java) {
            Transfer(
                "some account",
                "someAnotherAccount",
                Money.of(20, "PLN"),
                nonBankingDate
            )
        }
    }

    @Test
    fun transfer_should_not_be_created_when_amount_exceeds_limit() {
        assertThrows(TransferCreationException::class.java) {
            Transfer(
                "some account",
                "someAnotherAccount",
                Money.of(10000000, "PLN"),
                bankingDate
            )
        }
    }

}
