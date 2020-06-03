package com.cdsg.ficheaqui.data.network.retrofit.main

import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateViewModel

class MainRepoImpl(private val prefs: PreferencesProvider) : IMainRepo {

    override fun getAlarmSetTime() : Long {
        return prefs.getAlarmSetTime()
    }

    override fun setCurrentTime(time: Long) {

    }

    override fun setSecondsRemaining(seconds: Long) {
        prefs.setAlarmSetTime(seconds)
    }

    override fun getSecondsRemaining(): Long {
       return prefs.getSecondsRemaining()
    }

    override fun getTodayWorkTimes(): String {
        return prefs.getTodayWorkTimes()
    }

    override fun setTodayWorkTimes(time: String) {
        prefs.setTodayWorkTimes(time)
    }

    override fun setEnterTime(time: String) {
        prefs.setEnterTime(time)
    }

   override fun saveWorkTimeState(value: WorkTimeStateViewModel.WorkTimeState) {
         prefs.setWorkTimeState(value)
    }

    override fun getWorkTimeState(): WorkTimeStateViewModel.WorkTimeState {
        return prefs.getWorkTimeState()
    }

    override fun getEnterTime(): String {
        return prefs.getEnterTime()
    }
}