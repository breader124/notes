package com.breader.mortgage.tester

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.logging.Level
import java.util.logging.Logger

@Component
class RatingTester(
    private val kafkaTemplate: KafkaTemplate<String, Int>,
    @Value("\${topic.rating-filtered}") private val ratingFilteredTopic: String
) {

    companion object {
        private val logger = Logger.getLogger(this::class.java.name)
    }

    @KafkaListener(
        topics = ["\${topic.rating}"],
        groupId = "\${group.rating.id}",
        containerFactory = "intContainerFactory"
    )
    fun filterOut(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) key: String, @Payload rating: Int) {
        if (rating < 750) {
            logger.log(Level.INFO, "User with key: $key has score below low limit (750): $rating")
            return
        }

        kafkaTemplate.send(ratingFilteredTopic, key, rating)
    }

}
