package com.cdsg.ficheaqui.ui.login

interface AuthListener {
    fun onUserError(value:String)
    fun onPasswordError(value:String)
}