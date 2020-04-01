package com.gabo.ble.activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gabo.ble.R
import com.gabo.ble.ScreenState
import com.gabo.ble.databinding.ActivityRegistrationBinding
import com.gabo.ble.provideReceiverInfoRemoteRepository
import com.gabo.ble.provideReceiverInfoRepository
import com.gabo.ble.viewmodel.registration.RegistrationState
import com.gabo.ble.viewmodel.registration.RegistrationViewModel
import com.gabo.ble.viewmodel.registration.RegistrationViewModelFactory


class RegistrationActivity : AppCompatActivity() {

    private val viewModel : RegistrationViewModel by lazy {
            val factory =
                RegistrationViewModelFactory(
                    applicationContext.provideReceiverInfoRepository(),
                    applicationContext.provideReceiverInfoRemoteRepository()
                )
            ViewModelProvider(this@RegistrationActivity, factory).get(RegistrationViewModel::class.java)

    }

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.registerButton.setOnClickListener {
            if(isEmptyField(binding.fullName) or isEmptyField(binding.identification) or isEmptyField(binding.phoneNumber)){
                return@setOnClickListener
            }

            viewModel.registerUser(
                binding.fullName.text.toString(),
                binding.identification.text.toString(),
                binding.phoneNumber.text.toString()
            )
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
        viewModel.registrationState.observe(this, Observer {
            renderState(it)
        })

        viewModel.registeredReceiver.observe(this, Observer {
                (it as ScreenState.Render).renderState?.let {
                    renderState(ScreenState.Render(RegistrationState.Success))
                }
            }
        )

    }

    private fun isEmptyField(field: EditText): Boolean {
        val isEmpty = isEmpty(field.text.toString())
        if(isEmpty) field.error = getString(R.string.error_field_empty)
        return isEmpty
    }

    private fun renderState( screenState: ScreenState<RegistrationState>?) {
        when(screenState ){
            ScreenState.Loading -> showLoading()
            is ScreenState.Render -> processState(screenState.renderState)
        }

    }

    private fun processState(renderState: RegistrationState) {
        binding.progress.visibility = View.GONE
        when (renderState) {
            RegistrationState.Success ->{
                Intent(this, PermissionsActivity::class.java).apply {
                    this.flags = FLAG_ACTIVITY_CLEAR_TOP
                    this@RegistrationActivity.startActivity(this)
                }
                finish()
            }
            RegistrationState.Error -> binding.registerScreenTitle.error = getString(
                R.string.error_registering_receiver
            )
        }

    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
    }


}


