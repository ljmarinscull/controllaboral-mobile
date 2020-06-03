package com.cdsg.ficheaqui.domain.usescases.worktime

import com.cdsg.ficheaqui.data.NoWorked
import com.cdsg.ficheaqui.data.TimeData
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.vo.Resource

interface IWorkTimeUseCase {

    suspend fun saveWorkTime(values: HashMap<String, String>) : Resource<Boolean>
    suspend fun saveNotWorkedDay(value: NoWorked) : Resource<Boolean>
    suspend fun saveWorkTimeCorrected(values: List<String>) : Resource<Boolean>
    suspend fun getCompanyWorkTime() : Resource<List<TimeData>>

    fun getDay() : String
    fun getMonth() : String
    fun getYear() : String
    fun setKeyAndBooleanValue(key: String, value: Boolean)
    fun setCompanyWorkTime(value: String)
    fun getCompanyWorkTimeFromPrefs() : String
    fun getBooleanValueByKey(key: String) : Boolean
    fun getWorkTimeStatus(): WorkTimeViewModel.WorkTimeStatus
    fun setWorkTimeStatus(status: WorkTimeViewModel.WorkTimeStatus)
    fun getEnterTime(): String
    fun isWorkTimeAbleToChange() : Boolean
}