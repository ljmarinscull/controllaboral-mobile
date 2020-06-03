package com.cdsg.ficheaqui.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cdsg.ficheaqui.domain.usescases.main.IMainUseCase

class MainViewModelFactory(private val useCase: IMainUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass : Class<T>) : T {
        return modelClass.getConstructor(IMainUseCase::class.java).newInstance(useCase)
    }

}