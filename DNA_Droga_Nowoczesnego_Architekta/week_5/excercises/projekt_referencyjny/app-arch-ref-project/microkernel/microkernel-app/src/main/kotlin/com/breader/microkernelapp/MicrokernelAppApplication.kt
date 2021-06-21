package com.breader.microkernelapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class MicrokernelApp

fun main(args: Array<String>) {
    runApplication<MicrokernelApp>(*args)
}
