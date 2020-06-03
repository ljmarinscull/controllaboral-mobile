package com.cdsg.ficheaqui.ui.worktime

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.NoWorked
import com.cdsg.ficheaqui.data.OptionType
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.data.network.firebase.worktime.WorkTimeRepoImpl
import com.cdsg.ficheaqui.databinding.FragmentWorktimeBinding
import com.cdsg.ficheaqui.domain.usescases.worktime.WorkTimeUseCaseImpl
import com.cdsg.ficheaqui.ui.main.MainActivity
import com.cdsg.ficheaqui.ui.worktime.adapter.OptionTypeAdapter
import com.cdsg.ficheaqui.utils.*
import com.cdsg.ficheaqui.vo.Resource
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_worktime.*
import kotlinx.android.synthetic.main.fragment_worktime.progressBar

class WorkTimeFragment : Fragment() {

    private val workTimeViewModel by lazy {
        ViewModelProvider(this, WorkTimeViewModelFactory(
            WorkTimeUseCaseImpl(
                WorkTimeRepoImpl(PreferencesProvider(activity!!))
            )
        )).get(WorkTimeViewModel::class.java)
    }

    private lateinit var binding : FragmentWorktimeBinding

    private lateinit var todayOptions : AlertDialog
    private var isWorkTimeAbleToChange : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_worktime, container, false)

        val view = binding.root

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = workTimeViewModel

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workTimeViewModel.workTimeStatus.value = workTimeViewModel.getWorkTimeStatus()

        workTimeViewModel.workTimeStatus.observe(viewLifecycleOwner, Observer { value ->
            handleWorkTimeStatus(value)
        })

        isWorkTimeAbleToChange = workTimeViewModel.isWorkTimeAbleToChange()
    }

    private fun handleAction(optionType: OptionType) {

        if(OptionType.AUSENT == optionType || OptionType.HOLLIDAY == optionType || OptionType.VACATIONS == optionType){
            val value = NoWorked(optionType.getDBId())
            workTimeViewModel.saveNotWorkedDay(value)
        } else {
            workTimeView.changeEditStatus()
        }
    }

    private fun checkGPSPermission() {
        Dexter.withActivity(activity!!)
            .withPermissions(listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if(report.areAllPermissionsGranted()){
                        if(activity!!.isNotGPSActivated()){
                            activity!!.startActivityIntent<MainActivity>(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }
                }
                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>?, token: PermissionToken? ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun handleWorkTimeStatus(value: WorkTimeViewModel.WorkTimeStatus){
        when(value) {
            WorkTimeViewModel.WorkTimeStatus.WORKTIME_STEP0 -> {
                setupView()
            }
            WorkTimeViewModel.WorkTimeStatus.WORKTIME_STEP1 -> {
                val action = WorkTimeFragmentDirections.actionNavWorktimeToWorkTimeState()
                findNavController().navigate(action)
            }
            WorkTimeViewModel.WorkTimeStatus.WORKTIME_STEP2 -> {
                val action = WorkTimeFragmentDirections.actionNavWorktimeToInfo()
                findNavController().navigate(action)
            }
        }
    }

    private fun setupView() {
        val day = workTimeViewModel.getDay()
        val month = workTimeViewModel.getMonth()
        val year = workTimeViewModel.getYear()

        tvDay.text = day
        tvMonth.text = month
        tvYear.text = year

        bAccept.setOnClickListener {
            it.isEnabled = false
            val values = workTimeView.getTimesData()
            if (values.isNotEmpty()) {
                workTimeViewModel.saveWorkTimeCorrected(values)
                checkGPSPermission()
            } else {
                it.isEnabled = true
                toast("El Horario de trabajo no es válido. Porfavor, corríjalo.",0)
            }
        }

        bCorrect.setOnClickListener {
            showTodayOptionsDialog()
        }

       /*ivAdd.setOnClickListener {
            workTimeView.addWorkTime()
        }*/

        val worktimes = workTimeViewModel.getCompanyWorkTimeFromPrefs()
        if(worktimes.isEmpty()) {
            workTimeViewModel.companyWorktime.observe(viewLifecycleOwner, Observer { value ->
                when(value){
                    is Resource.Loading -> {
                        progressBar.showProgress()
                    }
                    is Resource.Success -> {
                        progressBar.hideProgress()
                        if(value.data.isNotEmpty()) {
                            workTimeView.setTimeDataList(value.data.toList())
                            workTimeViewModel.apply {
                                setCompanyWorkTime(value.data.toList())
                            }
                        } else {
                            toast("Ha ocurrido un error.",0)
                        }
                    }
                    is Resource.Failure -> {
                        progressBar.hideProgress()
                        toast("Ha ocurrido un error.",0)
                    }
                }
            })
        } else {
            workTimeView.setTimeDataList(worktimes)
        }

        workTimeViewModel.notWorkedDayStatus.observe(viewLifecycleOwner, Observer { value ->
            when(value){
                is Resource.Loading -> {
                    progressBar.showProgress()
                }
                is Resource.Success -> {
                    progressBar.hideProgress()
                    if(value.data) {
                        workTimeViewModel.setWorkTimeStatus(WorkTimeViewModel.WorkTimeStatus.WORKTIME_STEP2)
                    } else {
                        workTimeViewModel.setWorkTimeStatus(WorkTimeViewModel.WorkTimeStatus.WORKTIME_STEP2)
                        toast("No puede realizar operación.",0)
                    }
                }
                is Resource.Failure -> {
                    progressBar.hideProgress()
                    toast("No puede realizar operación.",0)
                }
            }
        })

        workTimeViewModel.workTimeCorrectedStatus.observe(viewLifecycleOwner, Observer { value ->
            when(value) {
                is Resource.Loading -> {
                    progressBar.showProgress()
                }
                is Resource.Success -> {
                    progressBar.hideProgress()
                    if(value.data) {
                        workTimeViewModel.setWorkTimeStatus(WorkTimeViewModel.WorkTimeStatus.WORKTIME_STEP1)
                    } else {
                        toast("Ha ocurrido un error.", 0)
                    }
                }
                is Resource.Failure -> {
                    progressBar.hideProgress()
                    toast("Ha ocurrido un error.", 0)
                }
            }
        })
    }

    private fun showTodayOptionsDialog() {

        val gridView = GridView(activity)
        gridView.setPadding(16,8,0,16)
        val  mList = mutableListOf<OptionType>(
            OptionType.AUSENT,
            OptionType.HOLLIDAY,
            OptionType.VACATIONS,
            OptionType.WORKTIME)

        if (!isWorkTimeAbleToChange){
            mList.removeAt(3)
        }

        gridView.adapter = OptionTypeAdapter(context!!,
            R.layout.custom_option_view, mList)
        gridView.numColumns = 2
        gridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, p2, _ ->
                handleAction(mList[p2])
                todayOptions.dismiss()
            }

        val builder =  AlertDialog.Builder(activity)
        builder.setView(gridView)
        builder.setTitle(getString(R.string.options))
        todayOptions = builder.create()
        todayOptions.show()
    }
}