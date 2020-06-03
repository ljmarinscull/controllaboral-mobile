package com.cdsg.ficheaqui.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.cdsg.ficheaqui.ui.main.MainViewModel
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateViewModel

private const val TIMER_STATE_ID  = "TIMER_STATE_ID"
private const val WORK_TIME_STATE_ID  = "WORK_TIME_STATE_ID"
private const val WORK_TIME_STATUS_ID  = "WORK_TIME_STATUS_ID"
private const val ALARM_SET_TIME_ID = "ALARM_SET_TIME_ID"
private const val END_OF_DAY_ALARM = "END_OF_DAY_ALARM"
private const val START_TIME = "START_TIME"
private const val END_TIME = "END_TIME"
private const val WORK_TIMES = "WORK_TIMES"
private const val ENTER_TIME = "ENTER_TIME"
private const val TOKEN = "TOKEN"
private const val CHANGE_WORK_TIME = "CHANGE_WORK_TIME"
private const val CURRENT_USER_NAME = "CURRENT_USER_NAME"
private const val COMPANY_WORKTIME = "COMPANY_WORKTIME"
private const val SECONDS_REMAINING_ID = "SECONDS_REMAINING_ID"
private const val TODAY_WORK_TIMES = "TODAY_WORK_TIMES"

class PreferencesProvider(mContext: Context) {

    private val appContext = mContext.applicationContext
    private val preference : SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    companion object {
        fun getTodayFromPrefs(ctx: Context) : String {
            val preferences = PreferenceManager.getDefaultSharedPreferences(ctx)
            var text = ""
            text +=  preferences.getString("day", "")
            text +=  preferences.getString("month", "")
            text +=  preferences.getString("year", "")
            return text
        }
    }

    fun setToday(day: String, month: String, year: String) {
        preference.edit().apply {
            putString("day", day)
            putString("month", month)
            putString("year", year)
            apply()
        }
    }

    fun getDay() : String {
        return  preference.getString("day", "")!!
    }

    fun getMonth() : String {
        return  preference.getString("month", "")!!
    }

    fun getYear() : String {
        return  preference.getString("year", "")!!
    }

    fun setWorkTimeStatus(status: Boolean) {
        preference.edit().apply {
            putBoolean("status", status)
            apply()
        }
    }

    fun setCompanyWorkTime(worktimes: String) {
        preference.edit().apply {
            putString(COMPANY_WORKTIME, worktimes)
            apply()
        }
    }

    fun getCompanyWorkTime(): String {
        return preference.getString(COMPANY_WORKTIME, "")!!
    }

    fun setKeyAndBooleanValue(key: String, value: Boolean) {
        preference.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBooleanValueByKey(key: String) : Boolean {
        return  preference.getBoolean(key, false)
    }

    fun getWorkTimeState() : WorkTimeStateViewModel.WorkTimeState {
        val ordinal = preference.getInt(WORK_TIME_STATE_ID, 0)
        return WorkTimeStateViewModel.WorkTimeState.values()[ordinal]
    }

    fun setWorkTimeState(state: WorkTimeStateViewModel.WorkTimeState) {
        val ordinal = state.ordinal
        preference.edit().apply {
            putInt(WORK_TIME_STATE_ID, ordinal)
            apply()
        }
    }

    fun getWorkTimeStatus(): WorkTimeViewModel.WorkTimeStatus {
        val ordinal = preference.getInt(WORK_TIME_STATUS_ID, 0)
        return WorkTimeViewModel.WorkTimeStatus.values()[ordinal]
    }

    fun setWorkTimeStatus(state: WorkTimeViewModel.WorkTimeStatus ) {
        val ordinal = state.ordinal
        preference.edit().apply {
            putInt(WORK_TIME_STATUS_ID, ordinal)
            apply()
        }
    }

    fun getAlarmSetTime(): Long {
        return  preference.getLong(ALARM_SET_TIME_ID, 0)
    }

    fun setAlarmSetTime(time: Long){
        preference.edit().apply {
            putLong(ALARM_SET_TIME_ID, time)
            apply()
        }
    }

    fun getSecondsRemaining(): Long {
        return preference.getLong(SECONDS_REMAINING_ID, 0)
    }

    fun getTodayWorkTimes(): String {
        return preference.getString(TODAY_WORK_TIMES, "")!!
    }

    fun setTodayWorkTimes(time: String) {
        preference.edit().apply {
            putString(TODAY_WORK_TIMES, time)
            apply()
        }
    }

    fun setCurrentUserName(username: String) {
        preference.edit().apply {
            putString(CURRENT_USER_NAME, username)
            apply()
        }
    }

    fun getCurrentUserName(): String {
        val username = preference.getString(CURRENT_USER_NAME, "")!!
        return if (username.isEmpty())
            "Armando Iglesias Garcia"
        else
            username
    }

    fun setEnterTime(time: String) {
        preference.edit().apply {
            putString(ENTER_TIME, time)
            apply()
        }
    }

    fun getEnterTime() : String {
        return preference.getString(ENTER_TIME, "")!!
    }

    fun setToken(token: String){
        preference.edit().apply {
            putString(TOKEN, token)
            apply()
        }
    }

    fun isWorkTimeAbleToChange() : Boolean {
        return preference.getBoolean(CHANGE_WORK_TIME, false)
    }

    fun setToken(value: Boolean){
        preference.edit().apply {
            putBoolean(CHANGE_WORK_TIME, value)
            apply()
        }
    }

    fun getToken() : String {
        val rawToken = preference.getString(TOKEN, "")!!
        return "Bearer $rawToken"
    }

    fun setTimerState(value: MainViewModel.TimerState){
        val ordinal = value.ordinal
        preference.edit().apply {
            putInt(TIMER_STATE_ID, ordinal)
            apply()
        }
    }

    fun getTimerState(): MainViewModel.TimerState {
        val ordinal = preference.getInt(TIMER_STATE_ID, 0)
        return MainViewModel.TimerState.values()[ordinal]
    }

    fun setupNewDay() {
        preference.edit().apply {
            putBoolean("status", false)
            putString(COMPANY_WORKTIME, "")
            putInt(WORK_TIME_STATE_ID, WorkTimeStateViewModel.WorkTimeState.None.ordinal)
            putString(TODAY_WORK_TIMES, "")
            putString(ENTER_TIME, "")
            apply()
        }
    }
}
