package com.breader.mortgage.processor

import com.breader.mortgage.source.IncomeDataReq
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.io.File
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.math.pow
import kotlin.random.Random

@Component
class ScoreCalculator(
    private val kafkaTemplate: KafkaTemplate<String, Int>,
    @Value("\${topic.income}") private val incomeInputTopic: String,
    @Value("\${topic.rating}") private val userRatingTopic: String
) {

    private val storageWriter = File("storage").bufferedWriter()

    companion object {
        private val logger = Logger.getLogger(this::class.java.name)
    }

    @KafkaListener(
        topics = ["\${topic.income}"],
        groupId = "\${group.income.id}",
        containerFactory = "containerFactory"
    )
    fun incomeInputListener(@Payload incomeData: IncomeDataReq) {
        logger.log(Level.INFO, "Received payload from $incomeInputTopic topic: $incomeData")

        val userId = UUID.randomUUID().toString()
        val score = calculateScore(incomeData)
        logger.log(Level.INFO, "User: $userId; Score: $score")

        saveToFile(userId, incomeData.phoneNumber)
        kafkaTemplate.send(userRatingTopic, userId, score)
    }

    fun calculateScore(incomeData: IncomeDataReq): Int {
        if (incomeData.constantCharges > incomeData.salary || incomeData.constantCharges < 0 || incomeData.salary < 0) {
            return 0
        }

        val init = (incomeData.constantCharges.toDouble() / incomeData.salary) * 1000
        val normalized = 0.001 * init.pow(2)
        val rawScore = (1000 - normalized).toInt()

        return rawScore.coerceIn(0, 1000)
    }

    fun saveToFile(userId: String, phoneNumber: String) = storageWriter.apply {
        append("$userId,$phoneNumber")
        appendLine()
        flush()
    }

}
