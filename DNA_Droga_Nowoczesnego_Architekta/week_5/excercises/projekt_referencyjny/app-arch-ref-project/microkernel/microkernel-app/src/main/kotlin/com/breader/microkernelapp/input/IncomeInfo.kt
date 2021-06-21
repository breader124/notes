package com.breader.microkernelapp.input

import java.time.LocalDate
import javax.money.MonetaryAmount

data class IncomeInfo(
    val averageLast3Months: MonetaryAmount,
    val averageLast12Months: MonetaryAmount,
    val peopleInHouseholdNum: Int,
    val employmentContractStart: LocalDate,
    val employmentContractTermination: LocalDate
)
