package com.cdsg.ficheaqui.ui.worktimestate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cdsg.ficheaqui.domain.usescases.worktimestate.IWorkTimeStateUseCase

class WorkTimeStateViewModelFactory(private val useCase: IWorkTimeStateUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass : Class<T>) : T {
        return modelClass.getConstructor(IWorkTimeStateUseCase::class.java).newInstance(useCase)
    }

}