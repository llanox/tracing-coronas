package com.gabo.ble.activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gabo.ble.R
import com.gabo.ble.databinding.ActivityPermissionsBinding

class PermissionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionsBinding
    private val requestFineLocation = 1010
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.switchBluetooth.setOnCheckedChangeListener { _, isChecked ->
            bluetoothAdapter?.apply {
                if (isChecked) enable() else disable()
            }
            checkPermissions()
        }
        binding.switchLocationServices.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                requestAccessFineLocation()
            }
            checkPermissions()
        }
        binding.continueButton.setOnClickListener {
            finish()
            Intent(this, MonitorActivity::class.java).apply {
                startActivity(this)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        checkPermissions()
    }

    private fun checkPermissions() {
        when {
             bluetoothAdapter == null ->showToast(getString(R.string.error_bluetooth_not_supported))
            !bluetoothAdapter.isEnabled -> binding.switchBluetooth.isChecked = false
            !isAccessFineLocationGranted() -> binding.switchLocationServices.isChecked = false
        }
        binding.continueButton.visibility = if (binding.switchBluetooth.isChecked && binding.switchLocationServices.isChecked) View.VISIBLE else View.INVISIBLE
    }

    private fun requestAccessFineLocation(){
       return ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), requestFineLocation)
    }
    private fun isAccessFineLocationGranted() : Boolean{
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            requestFineLocation -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    binding.switchLocationServices.isChecked = true
                } else {
                    showToast("NO ACCESS FINE LOCATION PERMISSION GRANTED")
                    binding.switchLocationServices.isChecked = false
                }
            }

        }
    }

    private fun showToast(msg:String){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }


}
