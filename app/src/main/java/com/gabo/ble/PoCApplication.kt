package com.gabo.ble

import android.app.Application
import com.gabo.ble.data.TracingDatabase
import com.gabo.ble.data.respository.*

class PoCApplication : Application() {


    val deviceTraceRepository: DeviceTraceRepository by lazy {
        val deviceTraceDAO = TracingDatabase.getDatabase(context = applicationContext).deviceTraceDao()
        DeviceTraceDatabaseRepository(deviceTraceDao = deviceTraceDAO)
    }

    val receiverInfoRepository : ReceiverInfoRepository by lazy {
        val receiverInfoDAO = TracingDatabase.getDatabase(context = applicationContext).receiverInfoDao()
        ReceiverDatabaseRepository(receiverInfoDAO = receiverInfoDAO)
    }


    val deviceTraceRemoteRepository: DeviceTraceRepository by lazy {
        DeviceTraceRemoteRepository()
    }

    val receiverInfoRemoteRepository : ReceiverInfoRepository by lazy {
        ReceiverRemoteRepository()
    }

}