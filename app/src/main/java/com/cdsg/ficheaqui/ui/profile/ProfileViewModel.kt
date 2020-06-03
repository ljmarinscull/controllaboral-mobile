package com.cdsg.ficheaqui.ui.profile

import androidx.lifecycle.*
import com.cdsg.ficheaqui.domain.usescases.auth.IAuthUseCase
import com.cdsg.ficheaqui.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val useCase: IAuthUseCase) : ViewModel() {

    private val _logOutStatus = MutableLiveData<Resource<Boolean>>()
    val logOutStatus : LiveData<Resource<Boolean>>?
        get() = _logOutStatus

    fun logOut(){
        viewModelScope.launch(Dispatchers.Main) {
            _logOutStatus.value = Resource.Loading()
            try {
                var result : Resource<Boolean>? = null
                withContext(Dispatchers.IO){
                    result = useCase.logout()
                }
                _logOutStatus.value = result
            } catch (e: Exception){
                _logOutStatus.value = Resource.Failure(e)
            }
        }
    }

    fun getCurrentUserNamePrefs() : String {
        return useCase.getCurrentUserNamePrefs()
    }
}