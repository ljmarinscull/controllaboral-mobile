package com.cdsg.ficheaqui.data.network.firebase.auth

import com.cdsg.ficheaqui.data.FirebaseUser
import com.cdsg.ficheaqui.data.LoginUserModel
import com.cdsg.ficheaqui.vo.Resource


interface IAuthRepo {

    suspend fun login(email: String, password: String) : Resource<FirebaseUser>?
    suspend fun logout() : Resource<Boolean>

    fun saveToday(day: String, month: String, year: String)
    fun getCurrentUserNamePrefs() : String
    fun setCurrentUserNamePrefs(value: String)
    fun setToken(token: String)
}