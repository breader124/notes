package com.breader.mortgage.sink.notifier

import org.springframework.stereotype.Component
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

@Component
class SmsUserNotifier : UserNotifier {

    private val storageReader = File("storage").bufferedReader()

    companion object {
        private val logger = Logger.getLogger(this::class.java.name)
    }

    override fun notify(userId: String) {
        storageReader.apply {
            lines()
                .map {
                    val split = it.split(",")
                    Pair(split[0], split[1])
                }
                .filter { it.first == userId }
                .findFirst()
                .ifPresent {
                    sendSms(it.second)
                }
        }
    }

    fun sendSms(phoneNumber: String) {
        logger.log(Level.INFO, "SMS has been sent to $phoneNumber")
    }

}
