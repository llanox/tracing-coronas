package com.gabo.ble

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.ParcelUuid
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.gabo.ble.activities.MonitorActivity
import com.gabo.ble.data.model.DeviceTrace
import com.gabo.ble.data.model.ReceiverInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import no.nordicsemi.android.support.v18.scanner.BluetoothLeScannerCompat
import no.nordicsemi.android.support.v18.scanner.ScanCallback
import no.nordicsemi.android.support.v18.scanner.ScanResult
import no.nordicsemi.android.support.v18.scanner.ScanSettings
import java.util.*

const val NOT_AVAILABLE = "N/A"
class BluetoothService : Service() {

    private val deviceTraceRepository by lazy { applicationContext.provideDeviceTraceRepository() }
    private val deviceTraceRemoteRepository by lazy { applicationContext.provideDeviceTraceRemoteRepository() }

    private val receiverInfoRepository by lazy { applicationContext.provideReceiverInfoRepository() }
    private lateinit var receiverInfo: ReceiverInfo
    private val scanner = BluetoothLeScannerCompat.getScanner()
    private val advertiser = BluetoothAdapter.getDefaultAdapter()?.bluetoothLeAdvertiser
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private val settings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .setReportDelay(0)
        .setPowerSave(5000, 2000)
        .setUseHardwareBatchingIfSupported(false)
        .build()

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            saveResult(result)
        }

        override fun onScanFailed(errorCode: Int) {
            Log.d("BLE", " scan has failed with error code: $errorCode")
        }

    }

    private val advertisingCallback = object : AdvertiseCallback() {

        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.d("BLE","Advertising started")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.d("BLE","Advertising failed $errorCode")
        }

    }


    private val observer = Observer<ReceiverInfo> {
        receiverInfo = it
        bluetoothAdapter.name = "${getString(R.string.prefix_device_name)}${it.receiverId.subSequence(0,4)}"
        startDeviceScanning()
        if(BluetoothAdapter.getDefaultAdapter()?.isMultipleAdvertisementSupported == true ){
            startAdvertising()
        }
    }

    private fun startAdvertising() {
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(false)
            .build()
        val parcelUuid = ParcelUuid(UUID.fromString(receiverInfo.receiverId))
        val advertiseData = AdvertiseData.Builder()
            .setIncludeDeviceName(true)
            .setIncludeTxPowerLevel(false)
            .addServiceUuid(parcelUuid)
            .build()

        advertiser?.startAdvertising(settings, advertiseData, advertisingCallback)
    }

    private fun startDeviceScanning() {
        scanner.startScan(null, settings, scanCallback)
    }

    private fun saveResult(result: ScanResult) {
        val name = "${result.scanRecord?.deviceName}"
        val serviceUUIDs = result.scanRecord?.serviceUuids
        val emitterId = if(!serviceUUIDs.isNullOrEmpty()) serviceUUIDs[0].uuid.toString() else NOT_AVAILABLE
        val epochMilliseconds = result.timestampNanos.convertNanoToMilliseconds()

        val deviceTrace =  DeviceTrace(
            emitterId = emitterId,
            receiverId = receiverInfo.receiverId,
            name = name,
            rssi = result.rssi,
            time = epochMilliseconds
        )

        serviceScope.launch {
            deviceTraceRepository.insert(deviceTrace)
            if (deviceTrace.name != null &&
                deviceTrace.name.contains(getString(R.string.prefix_device_name)) &&
                deviceTrace.emitterId != NOT_AVAILABLE) {
                deviceTraceRemoteRepository.insert(deviceTrace)
            }
        }

        Log.d("BLE","advertisingId ${result.advertisingSid} time ${Date(epochMilliseconds)} name ${result.scanRecord?.deviceName} rssi ${result.rssi} uuid $emitterId")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notifyIntent = Intent(this, MonitorActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Scanning devices around you")
            .setContentText("Discovering...")
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        receiverInfoRepository.getCurrent.observeForever(observer)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        val scanner = BluetoothLeScannerCompat.getScanner()
        scanner.stopScan(scanCallback)
        serviceJob.cancel()
        receiverInfoRepository.getCurrent.removeObserver(observer)
        advertiser?.stopAdvertising(advertisingCallback)
        stopSelf()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Scanning devices",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        fun startService(context: Context) {
            Intent(context, BluetoothService::class.java).apply {
                ContextCompat.startForegroundService(context, this)
            }
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, BluetoothService::class.java)
            context.stopService(stopIntent)
        }

        const val CHANNEL_ID = "device-scanner-channel"

    }

}