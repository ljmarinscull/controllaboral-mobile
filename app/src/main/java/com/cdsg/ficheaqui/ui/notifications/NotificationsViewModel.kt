package com.cdsg.ficheaqui.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.cdsg.ficheaqui.domain.usescases.notifications.INotificationsUseCase
import com.cdsg.ficheaqui.vo.Resource
import kotlinx.coroutines.Dispatchers

class NotificationsViewModel(private val useCase: INotificationsUseCase) : ViewModel() {

    val documentList = liveData (Dispatchers.IO){
        emit(Resource.Loading())
        try {
            val documents= useCase.getNotificactions()
            emit(documents)
        } catch (e: Exception){
            emit(Resource.Failure(e))
        }
    }
}