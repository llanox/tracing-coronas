package com.gabo.ble.data.respository

import androidx.lifecycle.LiveData
import com.gabo.ble.data.model.ReceiverInfo

interface ReceiverInfoRepository {
    val getCurrent : LiveData<ReceiverInfo>

    suspend fun insert(receiverInfo: ReceiverInfo)
}