package com.gabo.ble.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_trace")
class DeviceTrace (@PrimaryKey(autoGenerate = true) val id: Int = 0,
                   @ColumnInfo(name = "emitter_id") val emitterId: String,
                   @ColumnInfo(name = "receiver_id") val receiverId: String,
                   @ColumnInfo(name = "rssi") val rssi: Int,
                   @ColumnInfo(name = "name") val name: String?,
                   @ColumnInfo(name = "time") val time: Long
)

