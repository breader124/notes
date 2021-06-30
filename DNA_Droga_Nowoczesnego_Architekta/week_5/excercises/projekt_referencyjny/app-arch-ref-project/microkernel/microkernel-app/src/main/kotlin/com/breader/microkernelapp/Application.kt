package com.breader.microkernelapp

import com.breader.microkernelapp.kernel.KernelExternalConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(KernelExternalConfig::class)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
