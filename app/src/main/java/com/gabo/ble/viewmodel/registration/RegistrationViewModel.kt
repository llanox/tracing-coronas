package com.gabo.ble.viewmodel.registration
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.gabo.ble.ScreenState
import com.gabo.ble.data.model.ReceiverInfo
import com.gabo.ble.data.respository.ReceiverInfoRepository
import kotlinx.coroutines.launch
import java.util.*

class RegistrationViewModel(private val receiverDatabaseRepository: ReceiverInfoRepository, private val receiverRemoteRepository: ReceiverInfoRepository) : ViewModel() {
    private val _registrationState: MutableLiveData<ScreenState<RegistrationState>> = MutableLiveData()
    val registrationState: LiveData<ScreenState<RegistrationState>>
        get() = _registrationState

    private val _registeredReceiverState: MutableLiveData<ScreenState<ReceiverInfo?>> = MutableLiveData()
    val registeredReceiver: LiveData<ScreenState<ReceiverInfo?>>
        get() = _registeredReceiverState

    private val observer = Observer<ReceiverInfo?> {
        _registeredReceiverState.value = ScreenState.Render(it)
    }

    init {
        receiverDatabaseRepository.getCurrent.observeForever(observer)
    }

    override fun onCleared() {
        super.onCleared()
        receiverDatabaseRepository.getCurrent.removeObserver(observer)
    }


    fun registerUser( fullName: String, identification: String, phoneNumber: String) {
        val receiverInfo = ReceiverInfo(userIdentification = identification, receiverId = UUID.randomUUID().toString(), fullName = fullName, phoneNumber = phoneNumber )
        _registrationState.value = ScreenState.Loading
        viewModelScope.launch {
                try {
                    receiverDatabaseRepository.insert(receiverInfo)
                    receiverRemoteRepository.insert(receiverInfo)
                    _registrationState.value = ScreenState.Render(RegistrationState.Success)
                }catch (ex : Throwable){
                    _registrationState.value = ScreenState.Render(RegistrationState.Error)
                }
        }
    }
}
class RegistrationViewModelFactory(private val receiverDatabaseRepository: ReceiverInfoRepository, private val receiverRemoteRepository: ReceiverInfoRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RegistrationViewModel(
            receiverDatabaseRepository,
            receiverRemoteRepository
        ) as T
    }
}