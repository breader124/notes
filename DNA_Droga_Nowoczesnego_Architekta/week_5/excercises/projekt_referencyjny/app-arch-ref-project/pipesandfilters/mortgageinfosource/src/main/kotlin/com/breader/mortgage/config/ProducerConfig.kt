package com.breader.mortgage.config

import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin.NewTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class ProducerConfig(
    @Value("\${kafka.bootstrap.server}") private val bootstrapServerAddress: String,
    @Value("\${topic.income}") private val incomeInput: String,
    @Value("\${topic.rating}") private val userRating: String
) {

    @Bean
    fun inputMortgageTopic(): NewTopics = NewTopics(
        NewTopic(incomeInput, 3, 1),
        NewTopic(userRating, 3, 1)
    )

    @Bean
    fun jsonProducerFactory(): ProducerFactory<String, Any> = DefaultKafkaProducerFactory(
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServerAddress,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java
        )
    )

    @Bean
    fun jsonKafkaTemplate(): KafkaTemplate<String, Any> {
        return KafkaTemplate(jsonProducerFactory())
    }

    @Bean
    fun intProducerFactory(): ProducerFactory<String, Int> = DefaultKafkaProducerFactory(
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServerAddress,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to IntegerSerializer::class.java
        )
    )

    @Bean
    fun intKafkaTemplate(): KafkaTemplate<String, Int> {
        return KafkaTemplate(intProducerFactory())
    }

}
