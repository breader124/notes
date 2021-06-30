package com.breader.microkernelapp.processing

class ProcessorA : Processor {
    override fun process() {
        println("Processing with processor A")
    }
}

class ProcessorB : Processor {
    override fun process() {
        println("Processing with processor B")
    }
}

class ProcessorC : Processor {
    override fun process() {
        println("Processing with processor C")
    }
}

class DefaultProcessor : Processor {
    override fun process() {
        println("Processing with default processor")
    }
}
