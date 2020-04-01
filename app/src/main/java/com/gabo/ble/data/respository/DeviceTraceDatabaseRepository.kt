package com.gabo.ble.data.respository

import android.util.Log
import androidx.lifecycle.LiveData
import com.gabo.ble.data.dao.DeviceTraceDAO
import com.gabo.ble.data.model.DeviceTrace



class DeviceTraceDatabaseRepository (private val deviceTraceDao: DeviceTraceDAO) : DeviceTraceRepository {


    override val latestTraces: LiveData<List<DeviceTrace>> = deviceTraceDao.getLatestTraces()

    override suspend fun insert(deviceTrace: DeviceTrace) {
        try {
             deviceTraceDao.insert(deviceTrace)
        } catch (ex: Throwable) {
            Log.e("DeviceTraceRepository", "Error saving device trace ", ex)
        }
    }


}