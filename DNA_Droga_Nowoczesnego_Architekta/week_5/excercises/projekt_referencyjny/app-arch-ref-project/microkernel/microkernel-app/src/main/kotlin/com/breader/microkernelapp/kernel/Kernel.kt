package com.breader.microkernelapp.kernel

import com.breader.microkernelapp.input.IncomeInfo
import com.breader.microkernelapp.output.Output
import com.breader.microkernelapp.output.TestOutput
import com.breader.microkernelapp.processing.*
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
@RefreshScope
class Kernel(config: KernelExternalConfig) {

    private val processor: Processor = when (config.processor) {
        "A" -> ProcessorA()
        "B" -> ProcessorB()
        "C" -> ProcessorC()
        else -> DefaultProcessor()
    }

    private val output: Output = TestOutput()

    @EventListener
    fun handleInput(input: IncomeInfo) {
        processor.process()
        output.sendOut()
    }

}
