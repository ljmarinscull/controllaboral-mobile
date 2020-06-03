package com.cdsg.ficheaqui.ui.worktime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cdsg.ficheaqui.domain.usescases.worktime.IWorkTimeUseCase

class WorkTimeViewModelFactory(private val useCase: IWorkTimeUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass : Class<T>) : T {
        return modelClass.getConstructor(IWorkTimeUseCase::class.java).newInstance(useCase)
    }

}