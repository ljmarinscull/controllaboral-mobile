package com.cdsg.ficheaqui.ui.main.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.ui.main.MainActivity
import com.cdsg.ficheaqui.utils.toTwo
import java.text.SimpleDateFormat
import java.util.*

private const val NOTIFICATION_ID = 111

class TimerService : Service() {

    private val mHandler: Handler = Handler()
    private var mTimer: Timer? = null
    private var isTimerRunning : Boolean = false

    var intent: Intent? = null
    var counter : Long = 1000L

    // Service binder
    private val serviceBinder: IBinder = RunServiceBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return serviceBinder
    }

    override fun onCreate() {
        super.onCreate()
        //Log.v("Timer Service", "onCreate")
    }

    internal inner class TimeDisplayTimerTask : TimerTask() {
        override fun run() {
            mHandler.post(Runnable {
                calculeAndSendTime()
            })
        }
    }

    fun calculeAndSendTime()  {
        try {
       //     Log.v("Timer Service", "calculeAndSendTime")
            counter += 1000
            val seconds = counter / 1000 % 60
            val minutes = counter / (60 * 1000) % 60
            val hours = counter / (60 * 60 * 1000) % 24
            if (counter > 0) {
                val value = "${hours.toTwo()}:${minutes.toTwo()}:${seconds.toTwo()}"
                sendTime(value)
                if(value ==  "23:59:59"){
                    isTimerRunning = false
                    mTimer?.cancel()
                    mTimer?.purge()
                    stopSelf()
                }
            } else {
                mTimer!!.cancel()
            }
        } catch (e: Exception) {
            mTimer!!.cancel()
            mTimer!!.purge()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) : Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
      //  Log.e("Service finish", "Finish")
    }

    private fun sendTime(time: String) {
        intent!!.putExtra("time", time)
        sendBroadcast(intent)
    }

    fun isTimerRunning() : Boolean {
        return isTimerRunning
    }

    /**
     * Place the service into the foreground
     */
    fun foreground() {
        startForeground(NOTIFICATION_ID, createNotification())
    }

    /**
     * Return the service to the background
     */
    fun background() {
        stopForeground(true)
    }

    fun startTimer(value: Long) {
        counter = value
        if (!isTimerRunning) {
            isTimerRunning = true
            mTimer = Timer()
            mTimer!!.scheduleAtFixedRate(
                TimeDisplayTimerTask(),
                5,
                NOTIFY_INTERVAL
            )
            intent = Intent(str_receiver)
        } else {
            Log.e("TimerService", "startTimer request for an already running timer")
        }
    }

    fun stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false
            mTimer?.cancel()
            mTimer?.purge()
            stopSelf()
        } else {
            Log.e("TimerService", "stopTimer request for a timer that isn't running")
        }
    }

    inner class RunServiceBinder : Binder() {
        fun getService() : TimerService {
            return this@TimerService
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String) : String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = getColor(android.R.color.darker_gray)
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun createNotification() : Notification {
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel("my_service", "My Background Service")
        } else {
            ""
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Fiche Aquí")
            .setContentText("Actualiza el estado de tu jornada laboral.")
            .setSmallIcon(R.mipmap.ic_launcher)

        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent =
            PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(resultPendingIntent)

        return builder.build()
    }

    companion object {
        var str_receiver = "com.cdsg.ficheaqui.ui.main.service.timerservice"
        const val NOTIFY_INTERVAL: Long = 1000
    }
}