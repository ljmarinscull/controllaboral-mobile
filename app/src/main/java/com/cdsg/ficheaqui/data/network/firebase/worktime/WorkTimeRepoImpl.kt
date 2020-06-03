package com.cdsg.ficheaqui.data.network.firebase.worktime

import com.cdsg.ficheaqui.data.NoWorked
import com.cdsg.ficheaqui.data.TimeData
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.utils.*
import com.cdsg.ficheaqui.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class WorkTimeRepoImpl(private val prefs: PreferencesProvider) : IWorkTimeRepo {

    override suspend fun saveWorkTime(values: HashMap<String, String>) : Resource<Boolean> {
        val email = FirebaseAuth.getInstance().currentUser?.email!!
        FirebaseFirestore
            .getInstance().
                collection(CONTROLLABORAL).document(email)
            .set(values).await()
        return Resource.Success(true)
    }

    override suspend fun saveNotWorkedDay(value: NoWorked): Resource<Boolean> {
        val email = FirebaseAuth.getInstance().currentUser?.email!!
        val today = "${prefs.getMonth()}-${prefs.getDay()}-${prefs.getYear()}"

        if(today.isNotEmpty()){

            val dayRef = FirebaseFirestore
                .getInstance()
                .collection(CONTROLLABORAL)
                .document(email)
                .collection(DAYS)
                .document(today)

            val docSnapshot = dayRef.get().await()

            if(docSnapshot.exists()){
                return Resource.Success(false)
            } else {
                FirebaseFirestore
                    .getInstance()
                    .collection(CONTROLLABORAL)
                    .document(email)
                    .collection(DAYS)
                    .document(today)
                    .set(value).await()
                return Resource.Success(true)
            }
        } else {
            return Resource.Success(false)
        }
    }

    override suspend fun saveWorkTimeCorrected(values: List<String>) : Resource<Boolean> {
        val record = todayRecordStr()
        val email = FirebaseAuth.getInstance().currentUser?.email!!
        val data = hashMapOf(
            DATA to values,
            NOTY_TYPE to WORKTIME_CORRECTED
        )
        FirebaseFirestore
            .getInstance()
            .collection(NOTIFICATIONS)
            .document(record)
            .collection(EMPLOYEES)
            .document(email)
            .set(data, SetOptions.merge()).await()
        return Resource.Success(true)
    }

    override suspend fun getCompanyWorkTime(): Resource<List<TimeData>> {
        val result = FirebaseFirestore
            .getInstance()
            .collection(COMPANY)
            .document(WORKTIME).get().await()

        @Suppress("UNCHECKED_CAST")
        val values = result.data?.get(WORKTIME) as ArrayList<String>
        val newResult = mutableListOf<TimeData>()
        values.forEach { it -> newResult.add(TimeData(it)) }

        return Resource.Success(newResult)
    }

    override fun getDay(): String {
        return prefs.getDay()
    }

    override fun getMonth(): String {
        return prefs.getMonth()
    }

    override fun getYear(): String {
        return prefs.getYear()
    }


    override fun setCompanyWorkTime(value: String) {
        prefs.setCompanyWorkTime(value)
    }

    override fun setKeyAndBooleanValue(key: String, value: Boolean) {
        prefs.setKeyAndBooleanValue(key,value)
    }

    override fun setWorkTimeStatus(status: WorkTimeViewModel.WorkTimeStatus) =  prefs.setWorkTimeStatus(status)
    override fun getWorkTimeStatus() = prefs.getWorkTimeStatus()


    override fun getCompanyWorkTimeFromPrefs(): String {
        return prefs.getCompanyWorkTime()
    }

    override fun getBooleanValueByKey(key: String) : Boolean {
        return prefs.getBooleanValueByKey(key)
    }

    /*override fun getWorkTimeState(): MainViewModel.WorkTimeState {
        return prefs.getWorkTimeState()
    }*/

    override fun getEnterTime() : String {
        return prefs.getEnterTime()
    }

    override fun isWorkTimeAbleToChange(): Boolean {
        return prefs.isWorkTimeAbleToChange()
    }
}