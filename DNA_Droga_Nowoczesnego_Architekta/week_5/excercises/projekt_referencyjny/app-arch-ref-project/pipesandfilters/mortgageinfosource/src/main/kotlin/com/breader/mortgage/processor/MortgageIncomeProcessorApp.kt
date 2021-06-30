package com.breader.mortgage.processor

import com.breader.mortgage.config.CommunicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(CommunicationConfig::class)
class MortgageIncomeProcessorApp

fun main() {
    runApplication<MortgageIncomeProcessorApp>()
}
