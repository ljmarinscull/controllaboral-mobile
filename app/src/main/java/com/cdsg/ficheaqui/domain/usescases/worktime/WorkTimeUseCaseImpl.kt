package com.cdsg.ficheaqui.domain.usescases.worktime

import com.cdsg.ficheaqui.data.EnterData
import com.cdsg.ficheaqui.data.NoWorked
import com.cdsg.ficheaqui.data.TimeData
import com.cdsg.ficheaqui.data.network.firebase.worktime.IWorkTimeRepo
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.vo.Resource

class WorkTimeUseCaseImpl(private val repo: IWorkTimeRepo) : IWorkTimeUseCase {

    override suspend fun saveWorkTime(values:HashMap<String,String>) = repo.saveWorkTime(values)

    override suspend fun saveNotWorkedDay(value: NoWorked): Resource<Boolean> = repo.saveNotWorkedDay(value)

    override suspend fun saveWorkTimeCorrected(values: List<String>) =  repo.saveWorkTimeCorrected(values)

    override suspend fun getCompanyWorkTime(): Resource<List<TimeData>> =  repo.getCompanyWorkTime()


    override fun getDay() : String  = repo.getDay()

    override fun getMonth(): String = repo.getMonth()

    override fun getYear(): String = repo.getYear()

    override fun setWorkTimeStatus(status: WorkTimeViewModel.WorkTimeStatus) = repo.setWorkTimeStatus(status)

    override fun setKeyAndBooleanValue(key: String, value: Boolean) = repo.setKeyAndBooleanValue(key,value)

    override fun setCompanyWorkTime(value: String) = repo.setCompanyWorkTime(value)

    override fun getCompanyWorkTimeFromPrefs() = repo.getCompanyWorkTimeFromPrefs()

    override fun getBooleanValueByKey(key: String) = repo.getBooleanValueByKey(key)

    override fun getWorkTimeStatus(): WorkTimeViewModel.WorkTimeStatus = repo.getWorkTimeStatus()


    //override fun getWorkTimeState(): MainViewModel.WorkTimeState = repo.getWorkTimeState()

    override fun getEnterTime(): String = repo.getEnterTime()

    override fun isWorkTimeAbleToChange() = repo.isWorkTimeAbleToChange()

}