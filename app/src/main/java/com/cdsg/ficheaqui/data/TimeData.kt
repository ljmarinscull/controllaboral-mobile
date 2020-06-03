package com.cdsg.ficheaqui.data

class TimeData(time :String){

    private var startHour: String
    private var startMinute: String

    private var endHour: String
    private var endMinute: String

    private var startTime: String
    private var endTime: String

    init {
        val times = time.split("-")

        startTime = times[0]
        endTime = times[1]

        val startData = times[0].split(":")
        startHour = startData[0]
        startMinute = startData[1]

        val endData = times[1].split(":")
        endHour = endData[0]
        endMinute = endData[1]
    }

    fun setStartHourAndMinute(hour: Int, minute: Int){
        startHour = hour.toString()
        startMinute = minute.toString()
        startTime = "${getStartHourStr()}:${getStartMinuteStr()}"
    }

    fun setEndHourAndMinute(hour: Int, minute: Int){
        endHour = hour.toString()
        endMinute = minute.toString()
        endTime = "${getEndHourStr()}:${getEndMinuteStr()}"
    }

    fun getStartHourStr() = if(startHour.toInt() < 10) "0$startHour" else startHour
    fun getStartHour() = startHour.toInt()

    fun getStartMinuteStr() = if(startMinute == "0") "00" else startMinute
    fun getStartMinute() = startMinute.toInt()

    fun getEndHourStr() = if(endHour.toInt() < 10) "0$endHour" else endHour
    fun getEndHour() = endHour.toInt()

    fun getEndMinuteStr() = if(endMinute == "0") "00" else endMinute
    fun getEndMinute() = endMinute.toInt()

    fun getStartTime() = startTime
    fun getEndTime() = endTime

    fun getMinutesTotal() : Int {
        val startMinutes = (startHour.toInt() * 60) + startMinute.toInt()
        val endMinutes = (endHour.toInt() * 60) + endMinute.toInt()

        val result = endMinutes - startMinutes

        return result
    }

    fun getTimeData() = "$startTime-$endTime"

    fun isValid() : Boolean {
        if (getStartHour() < getEndHour()){
            return true
        } else if(getStartHour() == getEndHour()){
            if(getEndMinute() > getStartMinute())
                return true
            return false
        } else {
            return false
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeData

        if (startHour != other.startHour) return false
        if (startMinute != other.startMinute) return false
        if (endHour != other.endHour) return false
        if (endMinute != other.endMinute) return false
        if (startTime != other.startTime) return false
        if (endTime != other.endTime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = startHour.hashCode()
        result = 31 * result + startMinute.hashCode()
        result = 31 * result + endHour.hashCode()
        result = 31 * result + endMinute.hashCode()
        result = 31 * result + startTime.hashCode()
        result = 31 * result + endTime.hashCode()
        return result
    }
}
