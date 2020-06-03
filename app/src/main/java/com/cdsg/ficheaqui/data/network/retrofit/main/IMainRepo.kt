package com.cdsg.ficheaqui.data.network.retrofit.main

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
}