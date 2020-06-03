package com.cdsg.ficheaqui.ui.worktime

import androidx.lifecycle.*
import com.cdsg.ficheaqui.data.EnterData
import com.cdsg.ficheaqui.data.NoWorked
import com.cdsg.ficheaqui.data.TimeData
import com.cdsg.ficheaqui.domain.usescases.worktime.IWorkTimeUseCase
import com.cdsg.ficheaqui.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkTimeViewModel(private val useCase: IWorkTimeUseCase) : ViewModel() {

    enum class WorkTimeStatus {
        WORKTIME_STEP0,
        WORKTIME_STEP1,
        WORKTIME_STEP2
    }

    val workTimeStatus =
        MutableLiveData<WorkTimeStatus>(WorkTimeStatus.WORKTIME_STEP0)

    private val _workTimeCorrectedStatus = MutableLiveData<Resource<Boolean>>()
    val workTimeCorrectedStatus : LiveData<Resource<Boolean>>
        get() = _workTimeCorrectedStatus

    private val _notWorkedDayStatus = MutableLiveData<Resource<Boolean>>()
    val notWorkedDayStatus : LiveData<Resource<Boolean>>
        get() = _notWorkedDayStatus


    val companyWorktime = liveData (Dispatchers.IO){
        emit(Resource.Loading())
        try {
            val documents= useCase.getCompanyWorkTime()
            emit(documents)
        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }

    fun saveWorkTimeCorrected(values: String) {
        val string = useCase.getCompanyWorkTimeFromPrefs()
        if(string != values){
            val resultList = values.split("|")
            viewModelScope.launch(Dispatchers.Main) {
                _workTimeCorrectedStatus.value = Resource.Loading()
                try {
                    var result : Resource<Boolean>? = null
                    withContext(Dispatchers.IO) {
                        result = useCase.saveWorkTimeCorrected(resultList)
                    }
                    _workTimeCorrectedStatus.value = result!!
                } catch (e: Exception) {
                    _workTimeCorrectedStatus.value = Resource.Failure(e)
                }
            }
        } else {
            _workTimeCorrectedStatus.value = Resource.Success(true)
        }
    }

    fun saveNotWorkedDay(value: NoWorked){
        viewModelScope.launch(Dispatchers.Main) {
            _notWorkedDayStatus.value = Resource.Loading()
            try {
                var result : Resource<Boolean>? = null
                withContext(Dispatchers.IO) {
                    result = useCase.saveNotWorkedDay(value)
                }
                _notWorkedDayStatus.value = result!!
            } catch (e: Exception) {
                _notWorkedDayStatus.value = Resource.Failure(e)
            }
        }
    }

    //PREFERENCES METHODS
    fun getDay(): String {
        return useCase.getDay()
    }

    fun getMonth(): String {
        return useCase.getMonth()
    }

    fun getYear(): String {
        return useCase.getYear()
    }

    fun setWorkTimeStatus(status: WorkTimeStatus) {
        useCase.setWorkTimeStatus(status)
        workTimeStatus.value = status
    }

    fun setCompanyWorkTime(value: List<TimeData>) {
        val valueStr = convertTimeDataListToString(value)
        useCase.setCompanyWorkTime(valueStr)
    }

    fun getCompanyWorkTimeFromPrefs() : List<TimeData> {
        val valueStr = useCase.getCompanyWorkTimeFromPrefs()
        return convertStringToTimeDataList(valueStr)
    }

    private fun convertTimeDataListToString(list: List<TimeData>) : String {
        var result = ""
        for (item in list){
            result += "${item.getTimeData()}|"
        }
        return result.removeSuffix("|")
    }

    private fun convertStringToTimeDataList(list: String) : List<TimeData> {
        return if(list.isNotEmpty()){
            val result: List<String> = list.split("|")
            val values = mutableListOf<TimeData>()

            for (item in result) {
                values.add(TimeData(item))
            }
            values
        } else {
            emptyList()
        }
    }

    fun getWorkTimeStatus(): WorkTimeStatus {
        return useCase.getWorkTimeStatus()
    }

    fun getEnterTime() : String {
        return useCase.getEnterTime()
    }

    fun isWorkTimeAbleToChange() : Boolean {
        return useCase.isWorkTimeAbleToChange()
    }
}