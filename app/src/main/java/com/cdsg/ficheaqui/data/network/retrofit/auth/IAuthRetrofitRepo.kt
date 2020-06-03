package com.cdsg.ficheaqui.data.network.retrofit.auth

import com.cdsg.ficheaqui.data.FirebaseUser
import com.cdsg.ficheaqui.data.LoginUserModel
import com.cdsg.ficheaqui.vo.Resource

interface IAuthRetrofitRepo {

    suspend fun login(value : LoginUserModel) : Resource<FirebaseUser>?
    suspend fun logout(uid: String) : Resource<Boolean>
    fun saveToday(day: String, month: String, year: String)
    fun getCurrentUserNamePrefs() : String
    fun setCurrentUserNamePrefs(value: String)
    fun setToken(token: String)
}