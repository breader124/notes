package com.breader.hexagonal.config

import com.breader.hexagonal.adapter.InMemoryTransferRepo
import com.breader.hexagonal.adapter.InMemoryUserRepo
import com.breader.hexagonal.adapter.MockedTransferApi
import com.breader.hexagonal.usecase.CreatingTransfer
import com.breader.hexagonal.usecase.port.TransferApi
import com.breader.hexagonal.usecase.port.TransferRepo
import com.breader.hexagonal.usecase.port.UserRepo
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Bean
    fun transferRepo(): TransferRepo = InMemoryTransferRepo()

    @Bean
    fun userRepo(): UserRepo = InMemoryUserRepo()

    @Bean
    fun accountValidator(): TransferApi = MockedTransferApi()

    @Bean
    fun creatingTransfer(): CreatingTransfer = CreatingTransfer(userRepo(), transferRepo(), accountValidator())

}
