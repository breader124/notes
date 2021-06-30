package com.breader.mortgage.tester

import com.breader.mortgage.config.CommunicationConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(CommunicationConfig::class)
class RatingTesterApplication

fun main(args: Array<String>) {
    runApplication<RatingTesterApplication>(*args)
}
