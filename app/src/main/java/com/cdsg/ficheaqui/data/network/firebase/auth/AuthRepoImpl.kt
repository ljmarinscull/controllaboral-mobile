package com.cdsg.ficheaqui.data.network.firebase.auth

import com.cdsg.ficheaqui.data.FirebaseUser
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepoImpl(private val prefs: PreferencesProvider) : IAuthRepo {

    override suspend fun login(email: String, password: String): Resource<FirebaseUser>? {

        val result= FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email,password).await()

        val getTokenResult = result.user?.getIdToken(true)?.await()

        val token = getTokenResult?.token!!
        val user = result.user

        val firebaseUser = FirebaseUser(user?.email!!, user.displayName?:"", token)
        return Resource.Success(firebaseUser)
    }

    override suspend fun logout(): Resource<Boolean> {
        return try {
            FirebaseAuth.getInstance().signOut()
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