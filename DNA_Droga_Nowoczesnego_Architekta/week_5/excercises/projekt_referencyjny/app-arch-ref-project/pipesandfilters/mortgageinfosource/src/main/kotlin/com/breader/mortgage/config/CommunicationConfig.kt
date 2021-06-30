package com.breader.mortgage.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(ProducerConfig::class, ConsumerConfig::class)
class CommunicationConfig
