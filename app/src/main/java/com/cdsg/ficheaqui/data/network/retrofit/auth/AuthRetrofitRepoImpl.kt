package com.cdsg.ficheaqui.data.network.retrofit.auth

import com.cdsg.ficheaqui.data.FirebaseUser
import com.cdsg.ficheaqui.data.LoginUserModel
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.data.network.retrofit.AppApi
import com.cdsg.ficheaqui.data.network.retrofit.RetrofitService
import com.cdsg.ficheaqui.vo.Resource


class AuthRetrofitRepoImpl(private val prefs: PreferencesProvider) : IAuthRetrofitRepo {

    var client = RetrofitService.createService(AppApi::class.java)

    override suspend fun login(value : LoginUserModel): Resource<FirebaseUser> {

        val result= client.login(value)

        return Resource.Success(result)
    }

    override suspend fun logout(uid: String): Resource<Boolean> {
        return try {
            client.logout(uid)
            Resource.Success(true)
        } catch (e:Exception){
            Resource.Success(false)
        }
    }

    override fun saveToday(day: String, month: String, year: String) {
        prefs.setToday(day, month, year)
    }

    override fun getCurrentUserNamePrefs(): String {
        return prefs.getCurrentUserName()
    }

    override fun setCurrentUserNamePrefs(value: String) {
        prefs.setCurrentUserName(value)
    }

    override fun setToken(token: String) = prefs.setToken(token)
}