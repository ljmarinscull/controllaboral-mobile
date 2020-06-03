package com.cdsg.ficheaqui.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cdsg.ficheaqui.ui.main.MainActivity
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.data.network.firebase.auth.AuthRepoImpl
import com.cdsg.ficheaqui.databinding.ActivityLoginBinding
import com.cdsg.ficheaqui.domain.usescases.auth.AuthUseCaseImpl
import com.cdsg.ficheaqui.utils.*
import com.cdsg.ficheaqui.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.util.Calendar.*

class LoginActivity : AppCompatActivity(), AuthListener {

    private val loginViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory (
            AuthUseCaseImpl(
                AuthRepoImpl(PreferencesProvider(this))
            )
        )).get(AuthViewModel::class.java)
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            viewModel = loginViewModel
        }

        val day : String
        val month : String
        val year : String

        getInstance().apply {
            day = get(DAY_OF_MONTH).toString()
            month = monthStr(get(MONTH))
            year = get(YEAR).toString()
        }

        loginViewModel.saveToday(day,month,year)

        loginViewModel.setAuthListener(this)

        loginObserve()

        mbLogin.setOnClickListener {
            if(isOnline()) {
                loginViewModel.onLogin()
            } else {
                toast("No esta conectado a Internet. Revise su conexión.",1)
            }
        }
    }

    private fun loginObserve() {
        loginViewModel.loginStatus.observe(this, Observer { value ->
            when(value){
                is Resource.Loading -> {
                    progressBar.showProgress()
                    mbLogin.isEnabled = false
                }
                is Resource.Success -> {
                    progressBar.hideProgress()
                    if(value.data.email  == "ljmarinscull@gmail.com") {
                        val fullname = value.data.displayName
                        val token = value.data.token
                        loginViewModel.setCurrentUserName(fullname)
                        loginViewModel.setToken(token)
                        mStartActivity<MainActivity>()
                        finish()
                    } else {
                        toast("Credenciales Inválidas.",0)
                        mbLogin.isEnabled = true
                    }
                }
                is Resource.Failure -> {
                    progressBar.hideProgress()
                    toast("Ha ocurrido un error.",0)
                    mbLogin.isEnabled = true
                }
            }
        })
    }

    override fun onStart(){
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser != null ){
            mStartActivity<MainActivity>()
            finish()
        }
    }

    override fun onUserError(value: String) {
        toast(value,1)
        etUser.requestFocus()
    }

    override fun onPasswordError(value: String) {
        toast(value,1)
        etPassword.requestFocus()
    }
}