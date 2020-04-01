package com.gabo.ble.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gabo.ble.data.model.ReceiverInfo

@Dao
interface ReceiverInfoDAO {

    @Query("SELECT * FROM receiver_info LIMIT 1")
    fun getCurrent() : LiveData<ReceiverInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(receiverInfo: ReceiverInfo) : Long

}