package com.gabo.ble.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gabo.ble.data.model.DeviceTrace

@Dao
interface DeviceTraceDAO {

    @Query("SELECT * from device_trace ORDER BY id DESC LIMIT 1")
    fun getLatestTraces(): LiveData<List<DeviceTrace>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(deviceTrace: DeviceTrace) : Long

}
