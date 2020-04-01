package com.gabo.ble.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gabo.ble.data.dao.DeviceTraceDAO
import com.gabo.ble.data.dao.ReceiverInfoDAO
import com.gabo.ble.data.model.DeviceTrace
import com.gabo.ble.data.model.ReceiverInfo

@Database(entities = [DeviceTrace::class, ReceiverInfo::class], version = 1, exportSchema = false)
abstract class TracingDatabase : RoomDatabase() {

    abstract fun deviceTraceDao(): DeviceTraceDAO
    abstract fun receiverInfoDao(): ReceiverInfoDAO


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TracingDatabase? = null

        fun getDatabase(context: Context): TracingDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TracingDatabase::class.java,
                    "tracing_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}