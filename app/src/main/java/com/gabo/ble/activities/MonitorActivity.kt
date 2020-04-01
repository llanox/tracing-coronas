package com.gabo.ble.activities


import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gabo.ble.*
import com.gabo.ble.data.model.DeviceTrace
import com.gabo.ble.databinding.ActivityMonitorBinding
import com.gabo.ble.viewmodel.monitor.MonitorViewModel
import com.gabo.ble.viewmodel.monitor.MonitorViewModelFactory


class MonitorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonitorBinding
    private val traceMap = mutableMapOf<String, DeviceTrace>()

    private val viewModel : MonitorViewModel by lazy {
        val factory =
            MonitorViewModelFactory(
                applicationContext.provideDeviceTraceRepository()
            )
        ViewModelProvider(this@MonitorActivity, factory).get(MonitorViewModel::class.java)

    }

    private val outputObserver = Observer<ScreenState<List<DeviceTrace>>>{
        renderState(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.monitoringButton.setOnClickListener {
            updateView()
        }

        binding.monitorScreenTitle.setOnLongClickListener {
            binding.animationView.invertVisibility()
            binding.outputDebug.invertVisibility()
            binding.monitoringButton.invertVisibility()

            if(binding.outputDebug.isVisible()){
                viewModel.monitorState.observe(this, outputObserver)
            }else{
                viewModel.monitorState.removeObserver(outputObserver)
            }
            updateView()

            true
        }
        updateView()
        binding.outputDebug.movementMethod = ScrollingMovementMethod()
    }

    private fun updateView() {
        if (isServiceRunning(BluetoothService::class.java)) {
            BluetoothService.stopService(this)
            binding.monitoringButton.text = getText(R.string.start_button_text)
            binding.animationView.pauseAnimation()
        } else {
            BluetoothService.startService(this)
            binding.animationView.playAnimation()
            binding.monitoringButton.text = getText(R.string.stop_button_text)
            Log.d("BLE", "Service already running")
        }
    }

    private fun renderState(screenState: ScreenState<List<DeviceTrace>>?) {
       if (screenState is ScreenState.Render){
           screenState.renderState.forEach {
               traceMap[it.emitterId] = it
           }
           val buffer = StringBuffer()
           traceMap.toList().forEach {
               buffer.append("------------------------------------------------------------------\n")
               buffer.append("name:${it.second.name} \n")
               buffer.append("emitterId:${it.second.emitterId} \n")
               buffer.append("rssi: ${it.second.rssi} \n")
               buffer.append("-------------------------------------------------------------------\n")
           }

           binding.outputDebug.text = buffer.toString()

       }
    }

    override fun onResume() {
        super.onResume()
        startScannerService()
    }

    private fun startScannerService() {
        if (!isServiceRunning(BluetoothService::class.java)) {
            BluetoothService.startService(this)
        } else {
            Log.d("BLE", "Service already running")
        }
    }

}
