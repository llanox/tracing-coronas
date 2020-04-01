package com.gabo.ble.data.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "receiver_info")
class ReceiverInfo (@PrimaryKey(autoGenerate = true) val id: Int = 0,
                    @ColumnInfo(name = "user_id") val userIdentification: String = "",
                    @ColumnInfo(name = "receiver_id") val receiverId: String = "",
                    @ColumnInfo(name = "full_name") val fullName: String = "",
                    @ColumnInfo(name = "phone_number") val phoneNumber: String = "")