package com.cdsg.ficheaqui.data.network.firebase.worktimestate

import android.util.Log
import com.cdsg.ficheaqui.data.EnterData
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.data.WorkTimeDataTimer
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.utils.CONTROLLABORAL
import com.cdsg.ficheaqui.utils.DAYS
import com.cdsg.ficheaqui.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.concurrent.TimeUnit

class WorkTimeStateRepoImpl(private val prefs: PreferencesProvider) : IWorkTimeStateRepo {

    override fun getWorkTimeState() = prefs.getWorkTimeState()

    override suspend fun saveEnterTimeAndPosition(value: EnterData): Resource<WorkTimeDataTimer> {
        val email = FirebaseAuth.getInstance().currentUser?.email!!
        val today = "${prefs.getMonth()}-${prefs.getDay()}-${prefs.getYear()}"
        FirebaseFirestore
            .getInstance()
            .collection(CONTROLLABORAL)
            .document(email)
            .collection(DAYS)
            .document(today)
            .set(value, SetOptions.merge()).await()
        var longDate = 0L
                Calendar.getInstance().apply {
         longDate =
        TimeUnit.HOURS.toMillis(get(Calendar.HOUR_OF_DAY).toLong()) +
                TimeUnit.MINUTES.toMillis(get(Calendar.MINUTE).toLong()) +
                TimeUnit.SECONDS.toMillis(get(Calendar.SECOND).toLong()) +
                get(Calendar.MILLISECOND).toLong()
                }
            //Date(2020,2,16,21,45,0).time
        val result = WorkTimeDataTimer(longDate,longDate)
        return Resource.Success(result)
    }

    override fun setWorkTimeStatus(value: WorkTimeViewModel.WorkTimeStatus) {
        prefs.setWorkTimeStatus(value)
    }

}