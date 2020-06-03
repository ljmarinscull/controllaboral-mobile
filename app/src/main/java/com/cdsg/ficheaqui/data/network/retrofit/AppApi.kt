package com.cdsg.ficheaqui.data.network.retrofit

import com.cdsg.ficheaqui.data.FirebaseUser
import com.cdsg.ficheaqui.data.LoginUserModel
import com.cdsg.ficheaqui.vo.Resource
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppApi {

    @POST("/login")
    suspend fun login(@Body value: LoginUserModel) : FirebaseUser

    @GET("/logout")
    suspend fun logout(@Body uid: String): Boolean
}