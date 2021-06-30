package com.breader.mortgage.sink

import com.breader.mortgage.sink.notifier.UserNotifier
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class MortgageSink(private val notifier: UserNotifier) {

    @KafkaListener(
        topics = ["\${topic.rating-filtered}"],
        groupId = "\${group.rating-filtered.id}",
        containerFactory = "intContainerFactory"
    )
    fun notifyUser(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) userId: String, @Payload rating: Int) {
        notifier.notify(userId)
    }

}
