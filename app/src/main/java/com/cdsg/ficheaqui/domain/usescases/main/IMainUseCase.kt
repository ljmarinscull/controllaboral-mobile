package com.cdsg.ficheaqui.domain.usescases.main

import com.cdsg.ficheaqui.ui.main.MainViewModel
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateViewModel

interface IMainUseCase {

    fun setCurrentTime(time: Long)
    fun getAlarmSetTime() : Long
    fun setSecondsRemaining(seconds: Long)
    fun getSecondsRemaining() : Long
    fun getTodayWorkTimes() : String
    fun setTodayWorkTimes(time: String)
    fun setEnterTime(time: String)
    fun getEnterTime() : String
    fun saveWorkTimeState(value: WorkTimeStateViewModel.WorkTimeState)
    fun getWorkTimeState() : WorkTimeStateViewModel.WorkTimeState
    fun setTimerState(value: MainViewModel.TimerState)
    fun getTimerState() : MainViewModel.TimerState
}

