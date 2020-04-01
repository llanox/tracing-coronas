package com.gabo.ble.data.respository

import androidx.lifecycle.LiveData
import com.gabo.ble.data.model.DeviceTrace

interface DeviceTraceRepository {
    val latestTraces: LiveData<List<DeviceTrace>>

    suspend fun insert(deviceTrace: DeviceTrace)
}