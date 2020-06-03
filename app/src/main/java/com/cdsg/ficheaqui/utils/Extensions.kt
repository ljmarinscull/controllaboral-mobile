package com.cdsg.ficheaqui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cdsg.ficheaqui.ui.components.WorkTimeRowView
import java.util.*


inline fun <reified T: Activity> Activity.mStartActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

inline fun <reified T: Activity> Activity.startActivityIntent(intent: Intent) {
    startActivity(intent)
}

fun ProgressBar.showProgress(){
    visibility = VISIBLE
    (this.context as AppCompatActivity).window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun ProgressBar.hideProgress(){
    visibility = GONE
    (this.context as AppCompatActivity).window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, msg,duration).show()
}

fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_LONG){
    makeText(this, this.resources.getText(resId), duration).show()
}

fun Fragment.toast(resId: Int, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(activity!!, activity!!.resources.getText(resId), duration).show()
}

fun Fragment.toast(msg: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(activity!!, msg, duration).show()
}

/*
fun Context.setAlarm(nowSeconds: Long, secondsRemaining: Long) {
    val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, TimerExpiredReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
    PreferenceProvider.setAlarmSetTime(this,nowSeconds)
}

fun Context.setEndOfDayAlarm(endOfDayMiliSeconds: Long) {
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(this, TimerExpiredReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, endOfDayMiliSeconds, pendingIntent)
    PreferenceProvider.setEndOfDayAlarm(this,endOfDayMiliSeconds)
}

fun Context.removeAlarm(){
    val intent = Intent(this, TimerExpiredReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    PreferenceProvider.setAlarmSetTime(this,0)
}

fun Context.removeEndOfDayAlarm(){
    val intent = Intent(this, TimerExpiredReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
    PreferenceProvider.setEndOfDayAlarm(this,0)
}*/

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Fragment.isOnline(): Boolean {
    val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

fun Context.isNotGPSActivated() : Boolean {
    val manager =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager?

    return if (manager == null){
        true
    } else !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

fun monthStr(value:Int): String{
    if(value < 10)
        return "0${value + 1}"
    return value.toString()
}

fun Long.toTwo() : String {
    if(this < 10)
        return "0${this}"
        return this.toString()
}

fun todayRecordStr() : String {
    val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val month = Calendar.getInstance().get(Calendar.MONTH) + 1
    val year = Calendar.getInstance().get(Calendar.YEAR)
    return "$year-$month-$day"
}

fun Context.getTimeStrByMilliSeconds(mili: Long) : String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = mili
    return "${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}"
}

fun WorkTimeRowView.lessThan(item: WorkTimeRowView) : Boolean {
    if(getTimeDataObj().getEndHour() < item.getTimeDataObj().getStartHour()) {
        return true
    } else if(getTimeDataObj().getEndHour() == item.getTimeDataObj().getStartHour()) {
        if(getTimeDataObj().getEndMinute() < item.getTimeDataObj().getStartMinute())
            return true
    }
    return false
}

