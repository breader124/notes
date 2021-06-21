package com.breader.hexagonal.adapter

import com.breader.hexagonal.usecase.CreateTransferRequest
import com.breader.hexagonal.usecase.CreatingTransfer
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("transfer")
class HttpController(private val creatingTransfer: CreatingTransfer) {

    @PostMapping
    fun createTransfer(@RequestBody request: CreateTransferRequest) {
        creatingTransfer.createTransfer(request)
    }

}
