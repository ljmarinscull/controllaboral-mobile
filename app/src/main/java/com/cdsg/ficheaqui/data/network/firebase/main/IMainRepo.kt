package com.cdsg.ficheaqui.data.network.firebase.main

import com.cdsg.ficheaqui.ui.main.MainViewModel
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateViewModel

interface IMainRepo {

    fun getAlarmSetTime() : Long
    fun setCurrentTime(time: Long)

    fun setSecondsRemaining(seconds: Long)
    fun getSecondsRemaining(): Long

    fun getTodayWorkTimes() : String
    fun setTodayWorkTimes(time: String)
    fun setEnterTime(time: String)
    fun saveWorkTimeState(value: WorkTimeStateViewModel.WorkTimeState)
    fun getWorkTimeState() : WorkTimeStateViewModel.WorkTimeState
    fun getEnterTime() : String
    fun setTimerState(value: MainViewModel.TimerState)
    fun getTimerState() : MainViewModel.TimerState
}