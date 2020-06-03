package com.cdsg.ficheaqui.domain.usescases.auth

import com.cdsg.ficheaqui.data.FirebaseUser
import com.cdsg.ficheaqui.data.network.firebase.auth.IAuthRepo
import com.cdsg.ficheaqui.vo.Resource


class AuthUseCaseImpl(private val repo: IAuthRepo) :  IAuthUseCase {

    override suspend fun login(email: String, password: String): Resource<FirebaseUser>? =
        repo.login(email, password)

    override suspend fun logout(): Resource<Boolean> = repo.logout()

    override fun saveToday(day: String, month: String, year: String) = repo.saveToday(day,month,year)

    override fun getCurrentUserNamePrefs() = repo.getCurrentUserNamePrefs()

    override fun setCurrentUserNamePrefs(value: String) = repo.setCurrentUserNamePrefs(value)

    override fun setToken(token: String) = repo.setToken(token)
}