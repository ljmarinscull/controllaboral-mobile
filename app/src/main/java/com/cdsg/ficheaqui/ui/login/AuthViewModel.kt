package com.cdsg.ficheaqui.ui.login

import androidx.lifecycle.*
import com.cdsg.ficheaqui.data.LoginUserModel
import com.cdsg.ficheaqui.data.FirebaseUser
import com.cdsg.ficheaqui.domain.usescases.auth.IAuthUseCase
import com.cdsg.ficheaqui.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(private val useCase: IAuthUseCase) : ViewModel() {

    val user = MutableLiveData("")
    val password = MutableLiveData("")
    var listener: AuthListener? = null

    private val _loginStatus = MutableLiveData<Resource<FirebaseUser>>()
    val loginStatus : LiveData<Resource<FirebaseUser>>
        get() = _loginStatus


    fun onLogin(){
        val authUser = LoginUserModel(user.value!!.trim(), password.value!!.trim())

        if(authUser.isUserEmpty()){
            listener?.onUserError("Este campo es requerido.")
            return
        }

        if(authUser.isUserInvalid()){
            listener?.onUserError("Debe introducir un correo electrónico válido.")
            return
        }

        if(authUser.isPasswordEmpty()){
            listener?.onUserError("Este campo es requerido.")
            return
        }

        if(authUser.isPasswordInvalid()) {
            listener?.onUserError("La contraseña debe ser mayor a 8 caracteres.")
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            _loginStatus.value = Resource.Loading()
            try {
                var result : Resource<FirebaseUser>? = null
                withContext(Dispatchers.IO){
                    result = useCase.login(user.value!!, password.value!!)
                }
                _loginStatus.value = result!!
            } catch (e: Exception) {
                _loginStatus.value = Resource.Failure(e)
            }
        }
    }

    fun setAuthListener(value: AuthListener) {listener = value}

    fun saveToday(day: String, month: String, year: String) =
        useCase.saveToday(day,month,year)

    fun setToken(token: String) = useCase.setToken(token)

    fun setCurrentUserName(value: String) =
        useCase.setCurrentUserNamePrefs(value)
}