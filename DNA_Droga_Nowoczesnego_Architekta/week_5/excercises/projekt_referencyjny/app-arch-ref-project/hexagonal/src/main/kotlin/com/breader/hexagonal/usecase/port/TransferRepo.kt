package com.breader.hexagonal.usecase.port

import com.breader.hexagonal.domain.Transfer
import com.breader.hexagonal.domain.TransferId

// We need to store transfers in an event-log manner, in case of an audit we could fall in a big troubles not having
// this documented and ready to show
interface TransferRepo {
    fun findById(transferId: TransferId): Transfer?
    fun save(t: Transfer)
    fun delete(t: Transfer)
}
