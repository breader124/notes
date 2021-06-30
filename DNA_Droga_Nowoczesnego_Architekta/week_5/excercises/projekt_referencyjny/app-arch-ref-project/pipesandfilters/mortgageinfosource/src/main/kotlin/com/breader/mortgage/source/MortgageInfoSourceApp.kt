package com.breader.mortgage.source

import com.breader.mortgage.config.CommunicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(CommunicationConfig::class)
class MortgageInfoSourceApplication

fun main(args: Array<String>) {
    runApplication<MortgageInfoSourceApplication>(*args)
}
