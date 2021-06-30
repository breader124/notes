package com.breader.microkernelapp.input.http

import com.breader.microkernelapp.input.IncomeInfo
import org.javamoney.moneta.Money
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("input")
class HttpInput(val eventPublisher: ApplicationEventPublisher) {

    @PostMapping
    fun fetchInput(@RequestBody req: InputReq) {
        val info = IncomeInfo(
            averageLast3Months = Money.of(req.salaryRounded, "PLN"),
            averageLast12Months = Money.of(req.salaryRounded, "PLN"),
            peopleInHouseholdNum = 1,
            employmentContractStart = LocalDate.now().minusYears(1),
            employmentContractTermination = LocalDate.now().plusYears(2)
        )
        eventPublisher.publishEvent(info)
    }

}
