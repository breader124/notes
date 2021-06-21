package com.breader.hexagonal

import java.time.DayOfWeek
import java.time.LocalDate

// simplified as hell
fun LocalDate.isBankingDay() = !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
