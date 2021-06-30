package com.breader.mortgage.source

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MortgageController(
    private val template: KafkaTemplate<String, Any>,
    @Value("\${topic.income}") private val incomeInput: String
) {

    @PostMapping("income")
    fun fetchIncomeData(@RequestBody req: IncomeDataReq) {
        template.send(incomeInput, req)
    }

}

data class IncomeDataReq(
    val name: String,
    val salary: Long,
    val constantCharges: Long,
    val phoneNumber: String
)
