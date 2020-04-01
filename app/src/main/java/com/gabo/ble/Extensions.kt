package com.gabo.ble

import android.app.ActivityManager
import android.content.Context
import android.os.SystemClock
import android.view.View
import com.gabo.ble.data.respository.DeviceTraceRepository
import com.gabo.ble.data.respository.ReceiverInfoRepository

@SuppressWarnings("deprecation")
fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    val service = activityManager.getRunningServices(Integer.MAX_VALUE).firstOrNull {
        serviceClass.name == it.service.className
    }

   return service!=null
}

fun Context.provideDeviceTraceRepository(): DeviceTraceRepository {
    val poCApplication = applicationContext as PoCApplication
    return poCApplication.deviceTraceRepository
}

fun Context.provideDeviceTraceRemoteRepository(): DeviceTraceRepository {
    val poCApplication = applicationContext as PoCApplication
    return poCApplication.deviceTraceRemoteRepository
}

fun Context.provideReceiverInfoRepository(): ReceiverInfoRepository {
    val poCApplication = applicationContext as PoCApplication
    return poCApplication.receiverInfoRepository
}

fun Context.provideReceiverInfoRemoteRepository(): ReceiverInfoRepository {
    val poCApplication = applicationContext as PoCApplication
    return poCApplication.receiverInfoRemoteRepository
}

fun Long.convertNanoToMilliseconds() : Long {
    return System.currentTimeMillis() - SystemClock.elapsedRealtime() + this / 1000000
}
fun View.isVisible() : Boolean {
    return this.visibility == View.VISIBLE
}
fun View.invertVisibility() {
    when(this.visibility){
        View.VISIBLE -> this.visibility = View.INVISIBLE
        View.INVISIBLE -> this.visibility = View.VISIBLE
    }
}
