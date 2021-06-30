package com.breader.microkernelapp.output

import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class TestOutput : Output {

    override fun sendOut() {
        println("Sending out")
    }

}
