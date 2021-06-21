package com.breader.hexagonal.adapter

import com.breader.hexagonal.domain.Transfer
import com.breader.hexagonal.domain.TransferId
import com.breader.hexagonal.usecase.port.TransferRepo

class InMemoryTransferRepo : TransferRepo {

    private val db: MutableList<Transfer> = mutableListOf()

    override fun findById(transferId: TransferId): Transfer? = db.find { it.id == transferId }

    override fun save(t: Transfer) {
        db.add(t)
    }

    override fun delete(t: Transfer) {
        db.remove(t)
    }

}
