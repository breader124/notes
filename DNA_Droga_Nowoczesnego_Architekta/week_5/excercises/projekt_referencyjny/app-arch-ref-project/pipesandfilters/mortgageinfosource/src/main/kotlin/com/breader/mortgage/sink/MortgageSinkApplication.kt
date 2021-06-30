package com.breader.mortgage.sink

import com.breader.mortgage.config.CommunicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(CommunicationConfig::class)
class MortgageSinkApplication

fun main(args: Array<String>) {
    runApplication<MortgageSinkApplication>(*args)
}
