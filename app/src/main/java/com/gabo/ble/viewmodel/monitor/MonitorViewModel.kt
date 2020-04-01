package com.gabo.ble.viewmodel.monitor

import androidx.lifecycle.*
import com.gabo.ble.ScreenState
import com.gabo.ble.data.model.DeviceTrace
import com.gabo.ble.data.respository.DeviceTraceRepository


class MonitorViewModel(private val deviceTraceRepository: DeviceTraceRepository) : ViewModel(){

    private val _monitorState: MutableLiveData<ScreenState<List<DeviceTrace>>> = MutableLiveData()
    val monitorState: LiveData<ScreenState<List<DeviceTrace>>>
        get() = _monitorState

    private val observer = Observer<List<DeviceTrace>> {
        _monitorState.value = ScreenState.Render(it)
    }

    init {
        deviceTraceRepository.latestTraces.observeForever(observer)
    }


    override fun onCleared() {
        super.onCleared()
        deviceTraceRepository.latestTraces.removeObserver(observer)
    }


}
class MonitorViewModelFactory(private val deviceTraceRepository:DeviceTraceRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MonitorViewModel(
            deviceTraceRepository
        ) as T
    }
}