package com.breader.microkernelapp

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class MicrokernelApp : CommandLineRunner {

    @Value("\${cofnig.server.test}")
    private lateinit var configServerTest: String

    override fun run(vararg args: String?) {
        print(configServerTest)
    }
}

fun main(args: Array<String>) {
    runApplication<MicrokernelApp>(*args)
}
