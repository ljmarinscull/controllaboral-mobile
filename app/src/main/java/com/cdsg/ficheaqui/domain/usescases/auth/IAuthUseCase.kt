package com.cdsg.ficheaqui.domain.usescases.auth

import com.cdsg.ficheaqui.data.FirebaseUser
import com.cdsg.ficheaqui.vo.Resource

interface IAuthUseCase {

    suspend fun login(email: String, password: String) : Resource<FirebaseUser>?
    suspend fun logout() : Resource<Boolean>
    fun saveToday(day: String, month: String, year: String)
    fun getCurrentUserNamePrefs() : String
    fun setCurrentUserNamePrefs(value: String)
    fun setToken(token: String)
}