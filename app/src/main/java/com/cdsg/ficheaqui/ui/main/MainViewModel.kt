package com.cdsg.ficheaqui.ui.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cdsg.ficheaqui.domain.usescases.main.IMainUseCase
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateViewModel
import java.util.*

class MainViewModel(private val useCase: IMainUseCase) : ViewModel() {

    enum class TimerState {
        Stopped, Running
    }

    private var timerLengthSeconds: Long = 0

    private var timerState = TimerState.Stopped

    private var miliRemaining: Long = 1L

    private val _networkStatus = MutableLiveData<Boolean>(true)
    val networkStatus : LiveData<Boolean>
        get() = _networkStatus

    private val _location = MutableLiveData<Location>()
    val location : LiveData<Location>
        get() = _location

    private val _isFinished= MutableLiveData<Boolean>()
    val isFinished : LiveData<Boolean>
        get() = _isFinished

    private val _currentTime = MutableLiveData<String>()
    val currentTime : LiveData<String>
        get() = _currentTime

    fun setNetworkStatus(value : Boolean) {
        _networkStatus.value = value
    }

    fun setCurrentLocation(value : Location) {
        _location.value = value
    }

    fun setFinished(value : Boolean) {
        _isFinished.value = value
    }

    fun getRemainingTime(hours:Int,minutes:Int): Long {
        val minutesToMiliSec = (60 - minutes) * 60 * 1000
        val hourToMiliSec = (24 - hours) * (3600 * hours) * 1000
        val remainingTime  = minutesToMiliSec + hourToMiliSec

        return remainingTime.toLong()
    }

    fun getTimeStrByMilliSeconds(mili: Long): String {
        val cal = Calendar.getInstance()
            cal.timeInMillis = mili
        return "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}"
    }

    fun getAlarmSetTime() : Long {
        return useCase.getAlarmSetTime()
    }

    fun setSecondsRemaining(seconds: Long) {
        useCase.setSecondsRemaining(seconds)
    }

    fun getSecondsRemaining() : Long {
        return useCase.getSecondsRemaining()
    }

    fun setTimerState(value: TimerState ){
        timerState = value
        useCase.setTimerState(value)
    }

    fun getTimerState() : TimerState {
        return useCase.getTimerState()
    }

    fun setCurrentTime(value: String){
        _currentTime.value = value
    }

    fun getMiliRemaining() : Long {
        return miliRemaining
    }

    fun setTodayWorkTimes(time: String) {
        useCase.setTodayWorkTimes(time)
    }

    fun setEnterTime(time: String) {
        useCase.setEnterTime(time)
    }

    fun getEnterTime() : String {
        return useCase.getEnterTime()
    }

    fun getTodayWorkTimes() : String {
        return useCase.getTodayWorkTimes()
    }

    fun saveWorkTimeState(value : WorkTimeStateViewModel.WorkTimeState){
        useCase.saveWorkTimeState(value)
    }

    fun getWorkTimeState() : WorkTimeStateViewModel.WorkTimeState {
        return useCase.getWorkTimeState()
    }
}