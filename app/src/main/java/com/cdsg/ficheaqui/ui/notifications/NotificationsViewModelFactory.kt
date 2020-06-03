package com.cdsg.ficheaqui.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cdsg.ficheaqui.domain.usescases.notifications.INotificationsUseCase

class NotificationsViewModelFactory(private val useCase: INotificationsUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass : Class<T>) : T {
        return modelClass.getConstructor(INotificationsUseCase::class.java).newInstance(useCase)
    }
}