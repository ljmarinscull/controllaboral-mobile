package com.cdsg.ficheaqui.ui.worktimestate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdsg.ficheaqui.data.EnterData
import com.cdsg.ficheaqui.data.WorkTimeDataTimer
import com.cdsg.ficheaqui.domain.usescases.worktimestate.IWorkTimeStateUseCase
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkTimeStateViewModel(private val useCase: IWorkTimeStateUseCase) : ViewModel() {

    enum class WorkTimeState {
        None, Started, Pause, New, Finish
    }

    private val _startWorkTime = MutableLiveData<Boolean>().apply {
        value = true
    }
    val startWorkTime: LiveData<Boolean>
        get() = _startWorkTime

    private val _pauseWorkTime = MutableLiveData<Boolean>().apply {
        value = false
    }
    val pauseWorkTime: LiveData<Boolean>
        get() = _pauseWorkTime

    private val _resumeWorkTime = MutableLiveData<Boolean>().apply {
        value = false
    }
    val resumeWorkTime: LiveData<Boolean>
        get() = _resumeWorkTime

    private val _finishWorkTime = MutableLiveData<Boolean>().apply {
        value = false
    }

    val finishWorkTime: LiveData<Boolean>
        get() = _finishWorkTime

    private val _saveEnterTimeStatus = MutableLiveData<Resource<WorkTimeDataTimer>>()
    val saveEnterTimeStatus : LiveData<Resource<WorkTimeDataTimer>>
        get() = _saveEnterTimeStatus

    fun saveEnterTimeAndPosition(values: EnterData){
        viewModelScope.launch(Dispatchers.Main) {
            _saveEnterTimeStatus.value = Resource.Loading()
            try {
                var result : Resource<WorkTimeDataTimer>? = null
                withContext(Dispatchers.IO) {
                    result = useCase.saveEnterTimeAndPosition(values)
                }
                _saveEnterTimeStatus.value = result!!
            } catch (e: Exception) {
                _saveEnterTimeStatus.value = Resource.Failure(e)
            }
        }
    }

    fun startWorkTime() {
        _startWorkTime.value = false
        _pauseWorkTime.value = true
        _finishWorkTime.value = true
    }

    fun pauseWorkTime() {
        _pauseWorkTime.value = false
        _resumeWorkTime.value = true
    }

    fun resumeWorkTime() {
        _resumeWorkTime.value = false
        _pauseWorkTime.value = true
    }

    fun finishWorkTime() {
        _pauseWorkTime.value = false
        _finishWorkTime.value = false
        _resumeWorkTime.value = false
        _startWorkTime.value = true
    }

    fun setActionsStatus(value0: Boolean, value1: Boolean, value2: Boolean, value3: Boolean){
        _startWorkTime.value = value0
        _pauseWorkTime.value = value1
        _resumeWorkTime.value = value2
        _finishWorkTime.value = value3
    }

    fun getWorkTimeState() = useCase.getWorkTimeState()
    fun setWorkTimeStatus(value : WorkTimeViewModel.WorkTimeStatus) = useCase.setWorkTimeStatus(value)
}