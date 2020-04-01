package com.gabo.ble.data.respository

import android.util.Log
import androidx.lifecycle.LiveData
import com.gabo.ble.data.model.DeviceTrace
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
const val DEVICE_TRACES_COLLECTION ="deviceTraces"

class DeviceTraceRemoteRepository(private val database : FirebaseFirestore = Firebase.firestore) :DeviceTraceRepository{
    val TAG = "POC-DeviceTrace"
    override val latestTraces: LiveData<List<DeviceTrace>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override suspend fun insert(deviceTrace: DeviceTrace) {
        database.collection(DEVICE_TRACES_COLLECTION)
            .add(deviceTrace)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }


}