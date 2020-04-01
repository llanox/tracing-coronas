package com.gabo.ble.data.respository

import android.util.Log
import androidx.lifecycle.LiveData
import com.gabo.ble.data.model.ReceiverInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

const val RECEIVERS_COLLECTION ="receivers"

class ReceiverRemoteRepository(private val database : FirebaseFirestore = Firebase.firestore) : ReceiverInfoRepository {

    val TAG = "POC-Receiver"

    override val getCurrent: LiveData<ReceiverInfo>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override suspend fun insert(receiverInfo: ReceiverInfo) {
        database.collection(RECEIVERS_COLLECTION).add(receiverInfo)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }
}