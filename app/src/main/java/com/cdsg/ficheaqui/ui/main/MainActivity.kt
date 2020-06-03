package com.cdsg.ficheaqui.ui.main

import android.content.*
import android.location.Location
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.WorkTimeDataTimer
import com.cdsg.ficheaqui.data.local.preferences.PreferencesProvider
import com.cdsg.ficheaqui.data.network.firebase.main.MainRepoImpl
import com.cdsg.ficheaqui.domain.usescases.main.MainUseCaseImpl
import com.cdsg.ficheaqui.ui.main.service.TimerService
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateFragment
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateViewModel
import com.cdsg.ficheaqui.utils.getTimeStrByMilliSeconds
import com.cdsg.ficheaqui.utils.toast
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), WorkTimeStateFragment.WorkTimeStatus {

    private lateinit var locationRequest: LocationRequest
    private val locationCallback =  object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            if (locationResult == null) {
                return
            }
            handleLocation(locationResult.lastLocation)
        }
    }

    private val networkReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == "android.net.conn.CONNECTIVITY_CHANGE") {
                val connectivityManager  = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                if(networkInfo != null){
                    mainViewModel.setNetworkStatus(true)
                } else {
                    mainViewModel.setNetworkStatus(false)
                }
            }
        }
    }

    private val timerReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val strTime = intent.getStringExtra("time")
            mainViewModel.setCurrentTime(strTime)
            if(strTime  == "23:59:59"){
                onFinishWorkTime()
            }
        }
    }

    private val mainViewModel by lazy {
        ViewModelProvider(this, MainViewModelFactory (
            MainUseCaseImpl(
                MainRepoImpl(PreferencesProvider(this))
            )
        )).get(MainViewModel::class.java)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var timerService: TimerService? = null
    private var serviceBound = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 1000

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(this, R.id.nav_host_fragment)
        /*  Passing each menu ID as a set of Ids because each
            menu should be considered as top level destinations.
            val appBarCoiguration = AppBarConfiguration(setOf(
            R.id.navigation_worktime,
            R.id.navigation_notifications,
            R.id.navigation_profile
            ))
            setupActionBarWithNavController(navController, appBarConfiguration)*/ // disable menu if user not logged in

       /* if ( mainViewModel.getWorkTimeState() == MainViewModel.WorkTimeState.Finish ||
            ( mainViewModel.getWorkTimeState() == MainViewModel.WorkTimeState.None &&  PreferencesProvider.getTodayFromPrefs(this) == "$day$month$year"))
            navView.menu.getItem(0).isEnabled = false*/

        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        val i = Intent(this, TimerService::class.java)
        startService(i)
        bindService(i, mConnection, 0)
    }

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(networkReceiver, intentFilter)

        registerReceiver(timerReceiver, IntentFilter(TimerService.str_receiver))
        timerService?.background()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkReceiver)
        unregisterReceiver(timerReceiver)
    }

    override fun onStop(){
        super.onStop()

        if (serviceBound) {
            // If a timer is active, foreground the service, otherwise kill the service
            if (timerService!!.isTimerRunning()) {
                timerService!!.foreground()
            } else {
                stopService(Intent(this, TimerService::class.java))
            }
            // Unbind the service
            unbindService(mConnection)
            serviceBound = false
        }
    }

    fun isFinished(): LiveData<Boolean> = mainViewModel.isFinished
    fun getNetWorkStatus(): LiveData<Boolean> = mainViewModel.networkStatus
    fun getCurrentLocation(): LiveData<Location> = mainViewModel.location
    fun getCurrentTime(): LiveData<String> = mainViewModel.currentTime

    override fun onStartWorkTime() {
        toast("Nueva Jornada!")
        fusedLocationClient
            .lastLocation
            .addOnSuccessListener { location : Location? ->
                if(location != null) {
                    mainViewModel.setCurrentLocation(location)
                } else {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                }
            }

    }

    override fun onStartWorkTimeSaved(value: WorkTimeDataTimer) {
        val time = getTimeStrByMilliSeconds(value.enterTime)
        mainViewModel.setEnterTime(time)
        var todayWorkTimes = mainViewModel.getTodayWorkTimes()
        todayWorkTimes += time
        mainViewModel.setTodayWorkTimes(todayWorkTimes)
        mainViewModel.saveWorkTimeState(WorkTimeStateViewModel.WorkTimeState.Started)
        timerService?.startTimer(value.enterTime)
    }

    override fun onPauseWorkTime() {
        toast("Turno terminado!")
        var todayWorkTimes = mainViewModel.getTodayWorkTimes()
        todayWorkTimes += "-${mainViewModel.currentTime.value}"
        mainViewModel.setTodayWorkTimes(todayWorkTimes)
        mainViewModel.saveWorkTimeState(WorkTimeStateViewModel.WorkTimeState.Pause)
    }

    override fun onResumeWorkTime() {
        toast("Nueva Jornada!")
        var todayWorkTimes = mainViewModel.getTodayWorkTimes()
        todayWorkTimes += "|${mainViewModel.currentTime.value}"
        mainViewModel.setTodayWorkTimes(todayWorkTimes)
        mainViewModel.saveWorkTimeState(WorkTimeStateViewModel.WorkTimeState.New)
    }

    override fun onFinishWorkTime() {
        //toast("Jornada Terminada!")
        if(mainViewModel.getWorkTimeState() == WorkTimeStateViewModel.WorkTimeState.Started ||
            mainViewModel.getWorkTimeState() == WorkTimeStateViewModel.WorkTimeState.New) {
            var todayWorkTimes = mainViewModel.getTodayWorkTimes()
            todayWorkTimes += "-${mainViewModel.currentTime.value}"
            mainViewModel.setTodayWorkTimes(todayWorkTimes)
        }
        mainViewModel.saveWorkTimeState(WorkTimeStateViewModel.WorkTimeState.Finish)
        mainViewModel.setFinished(true)
        stopService(intent)
        timerService?.stopTimer()
        toast(mainViewModel.getTodayWorkTimes(),1)
    }

    private fun handleLocation(lastLocation: Location) {
        mainViewModel.setCurrentLocation(lastLocation)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    /**
     * Callback for service binding, passed to bindService()
     */
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder: TimerService.RunServiceBinder = service as TimerService.RunServiceBinder
            timerService = binder.getService()
            serviceBound = true
            // Ensure the service is not in the foreground when bound
            timerService?.background()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = false
        }
    }
}


