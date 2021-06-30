package com.breader.microkernelapp.kernel

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "config.kernel")
data class KernelExternalConfig(
    var processor: String = ""
)
