package com.cdsg.ficheaqui.ui.docdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cdsg.ficheaqui.domain.usescases.docdetails.IDocDetailsUseCase

class DocDetailsViewModelFactory (private val useCase: IDocDetailsUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass : Class<T>) : T {
        return modelClass.getConstructor(IDocDetailsUseCase::class.java).newInstance(useCase)
    }

}