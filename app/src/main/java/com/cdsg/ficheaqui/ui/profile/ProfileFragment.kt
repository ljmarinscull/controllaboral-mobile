package com.cdsg.ficheaqui.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.data.network.firebase.auth.AuthRepoImpl
import com.cdsg.ficheaqui.databinding.FragmentProfileBinding
import com.cdsg.ficheaqui.domain.usescases.auth.AuthUseCaseImpl
import com.cdsg.ficheaqui.ui.login.LoginActivity
import com.cdsg.ficheaqui.utils.*
import com.cdsg.ficheaqui.vo.Resource
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private val profileViewModel by lazy {
        ViewModelProvider(this, ProfileViewModelFactory(
            AuthUseCaseImpl(
                AuthRepoImpl(PreferencesProvider(activity!!))
            )
        )).get(ProfileViewModel::class.java)
    }

    private lateinit var binding : FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_profile, container, false)
        val view = binding.root
        //here data must be an instance of the class MarsDataProvider
        binding.viewModel = profileViewModel
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.logOutStatus?.observe(viewLifecycleOwner, Observer { value ->
            when(value){
                is Resource.Loading -> {
                    progressBar.showProgress()
                }
                is Resource.Success -> {
                    progressBar.hideProgress()
                    if(value.data) {
                        activity?.mStartActivity<LoginActivity>()
                        activity?.finish()
                    } else {
                        toast("Ha ocurrido un error. Intente cerrar la sesión.",0)
                    }
                }
                is Resource.Failure -> {
                    progressBar.hideProgress()
                    toast("Ha ocurrido un error.",0)
                }
            }
        } )

        tvUserName.text = profileViewModel.getCurrentUserNamePrefs()

        bCorrect.setOnClickListener {
            if(activity!!.isOnline()) {
                profileViewModel.logOut()
            } else {
                toast("No esta conectado a Internet. Revise su conexión.",1)
            }
        }
    }

}