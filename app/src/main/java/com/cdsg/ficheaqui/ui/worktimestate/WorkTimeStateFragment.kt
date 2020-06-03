package com.cdsg.ficheaqui.ui.worktimestate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.EnterData
import com.cdsg.ficheaqui.data.WorkTimeDataTimer
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.data.network.firebase.worktimestate.WorkTimeStateRepoImpl
import com.cdsg.ficheaqui.databinding.FragmentWorkTimeStateBinding
import com.cdsg.ficheaqui.domain.usescases.worktimestate.WorkTimeStateUseCaseImpl
import com.cdsg.ficheaqui.ui.main.MainActivity
import com.cdsg.ficheaqui.ui.main.MainViewModel
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.utils.hideProgress
import com.cdsg.ficheaqui.utils.showProgress
import com.cdsg.ficheaqui.utils.toast
import com.cdsg.ficheaqui.vo.Resource
import com.google.firebase.firestore.GeoPoint
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_work_time_state.*

class WorkTimeStateFragment : Fragment() {

    private val wtsViewModel by lazy {
        ViewModelProvider(this, WorkTimeStateViewModelFactory(
            WorkTimeStateUseCaseImpl(
                WorkTimeStateRepoImpl(PreferencesProvider(activity!!))
            )
        )).get(WorkTimeStateViewModel::class.java)
    }

    private val viewModelMain : MainViewModel by activityViewModels()

    private lateinit var binding : FragmentWorkTimeStateBinding
    private val listener by lazy {
        activity as WorkTimeStatus
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_work_time_state, container, false)

        val view = binding.root

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = wtsViewModel

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDay.text = "08"
        tvMonth.text = "03"
        tvYear.text = "2020"

        handleActionsStatus()

        bEnter.setOnClickListener {
            listener.onStartWorkTime()
        }

        bStopWorkTime.setOnClickListener {
            wtsViewModel.pauseWorkTime()
            listener.onPauseWorkTime()
        }

        bNewWorkTime.setOnClickListener {
            wtsViewModel.resumeWorkTime()
            listener.onResumeWorkTime()
        }

        bEnd.setOnClickListener {
            wtsViewModel.finishWorkTime()
            listener.onFinishWorkTime()
        }

        wtsViewModel.saveEnterTimeStatus.observe(viewLifecycleOwner, Observer { value ->
            when (value) {
                is Resource.Loading -> {
                    progressBar.showProgress()
                }
                is Resource.Success -> {
                    progressBar.hideProgress()
                    listener.onStartWorkTimeSaved(value.data)
                    wtsViewModel.startWorkTime()
                    currentTime.visibility = View.VISIBLE
                }
                is Resource.Failure -> {
                    progressBar.hideProgress()
                    toast("Ha ocurrido un error.", 0)
                }
            }
        })

        (activity as MainActivity).getCurrentLocation().observe(viewLifecycleOwner, Observer { value ->
            if (value != null) {
                val geo = GeoPoint(value.latitude, value.longitude)
                val time = viewModelMain.getEnterTime()
                val enterData = EnterData(geo, time)
              wtsViewModel.saveEnterTimeAndPosition(enterData)
            }
        })

        (activity as MainActivity).isFinished().observe(viewLifecycleOwner, Observer { value ->
            if(value){
                handleActionsStatus()
                val action = WorkTimeStateFragmentDirections.actionWorkTimeStateToInfo()
                findNavController().navigate(action)
                wtsViewModel.setWorkTimeStatus(WorkTimeViewModel.WorkTimeStatus.WORKTIME_STEP2)
            }
        })

        (activity as MainActivity).getCurrentTime().observe(viewLifecycleOwner, Observer {
            currentTime.text = it
        })
    }

    private fun handleActionsStatus() = when (wtsViewModel.getWorkTimeState()) {
        WorkTimeStateViewModel.WorkTimeState.None -> {
            wtsViewModel.setActionsStatus(
                value0 = true,
                value1 = false,
                value2 = false,
                value3 = false
            )
        }
        WorkTimeStateViewModel.WorkTimeState.Started -> {
            wtsViewModel.setActionsStatus(
                value0 = false,
                value1 = true,
                value2 = false,
                value3 = true
            )
            currentTime.visibility = View.VISIBLE
        }
        WorkTimeStateViewModel.WorkTimeState.Pause -> {
            wtsViewModel.setActionsStatus(
                value0 = false,
                value1 = false,
                value2 = true,
                value3 = true
            )
        }
        WorkTimeStateViewModel.WorkTimeState.New -> {
            wtsViewModel.setActionsStatus(
                value0 = false,
                value1 = true,
                value2 = false,
                value3 = true
            )
        }
        WorkTimeStateViewModel.WorkTimeState.Finish -> {
            wtsViewModel.setActionsStatus(
                value0 = true,
                value1 = false,
                value2 = false,
                value3 = false
            )
        }
    }

    interface WorkTimeStatus {
        fun onStartWorkTime()
        fun onStartWorkTimeSaved(value: WorkTimeDataTimer)
        fun onPauseWorkTime()
        fun onResumeWorkTime()
        fun onFinishWorkTime()
    }
}
