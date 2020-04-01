package com.gabo.ble.data.respository

import android.util.Log
import androidx.lifecycle.LiveData
import com.gabo.ble.data.dao.ReceiverInfoDAO
import com.gabo.ble.data.model.ReceiverInfo



class ReceiverDatabaseRepository (private val receiverInfoDAO: ReceiverInfoDAO) :
    ReceiverInfoRepository {

    override val getCurrent : LiveData<ReceiverInfo> = receiverInfoDAO.getCurrent()

    override suspend fun insert(receiverInfo: ReceiverInfo) {
            val rowId = receiverInfoDAO.insert(receiverInfo)
            Log.d("ReceiverInfoRepository","inserted a new record $rowId result ${receiverInfo.fullName}")

    }
}