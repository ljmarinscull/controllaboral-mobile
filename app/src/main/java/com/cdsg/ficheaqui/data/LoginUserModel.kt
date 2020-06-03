package com.cdsg.ficheaqui.data

import android.util.Patterns

class LoginUserModel (val user:String, val password:String){

    fun isUserInvalid(): Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(user).matches()
    }

    fun isUserEmpty(): Boolean {
        return user.isEmpty()
    }

    fun isPasswordEmpty(): Boolean {
        return password.isEmpty()
    }

    fun isPasswordInvalid(): Boolean {
        return password.length < 8
    }
}