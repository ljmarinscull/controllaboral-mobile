package com.cdsg.ficheaqui.data.network.retrofit.worktimestate

import com.cdsg.ficheaqui.data.EnterData
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.utils.CONTROLLABORAL
import com.cdsg.ficheaqui.utils.DAYS
import com.cdsg.ficheaqui.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class WorkTimeStateRepoImpl(private val prefs: PreferencesProvider) : IWorkTimeStateRepo {

    override fun getWorkTimeState() = prefs.getWorkTimeState()

    override suspend fun saveEnterTimeAndPosition(value: EnterData): Resource<Boolean> {
        val email = FirebaseAuth.getInstance().currentUser?.email!!
        val today = "${prefs.getMonth()}-${prefs.getDay()}-${prefs.getYear()}"
        FirebaseFirestore
            .getInstance()
            .collection(CONTROLLABORAL)
            .document(email)
            .collection(DAYS)
            .document(today)
            .set(value, SetOptions.merge()).await()
        return Resource.Success(true)
    }

    override fun setWorkTimeStatus(value: WorkTimeViewModel.WorkTimeStatus) {
        prefs.setWorkTimeStatus(value)
    }

}