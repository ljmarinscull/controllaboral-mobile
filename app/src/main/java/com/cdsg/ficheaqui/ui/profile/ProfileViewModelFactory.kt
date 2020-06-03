package com.cdsg.ficheaqui.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cdsg.ficheaqui.domain.usescases.auth.IAuthUseCase

class ProfileViewModelFactory(private val useCase: IAuthUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass : Class<T>) : T {
        return modelClass.getConstructor(IAuthUseCase::class.java).newInstance(useCase)
    }

}