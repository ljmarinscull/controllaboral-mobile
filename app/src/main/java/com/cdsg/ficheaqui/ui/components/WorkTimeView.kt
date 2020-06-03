package com.cdsg.ficheaqui.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.TimeData
import com.cdsg.ficheaqui.utils.lessThan
import kotlinx.android.synthetic.main.working_time_view.view.*

class WorkTimeView : LinearLayout, WorkTimeRowView.WorkTimeRowViewAct {

    private lateinit var mContext: Context
    private var mView : View
    private lateinit var llTimesContainer :LinearLayout
    private lateinit var tvTotalValue :TextView

    private lateinit var mTimeDataList: List<TimeData>
    private var workTimeRowViewList: MutableList<WorkTimeRowView> = mutableListOf()

    private var editStatus: Boolean = false
    private var minutes = 0

    constructor(context: Context) : super(context) {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mView = inflater.inflate(R.layout.working_time_view, this, true)

        initView(context)
    }

    constructor(context: Context,attr: AttributeSet): super (context,attr) {

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mView = inflater.inflate(R.layout.working_time_view, this, true)

        initView(context)
    }

    constructor(context: Context,attr: AttributeSet,defStyleAttr: Int): super (context,attr,defStyleAttr) {

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mView = inflater.inflate(R.layout.working_time_view, this, true)

        initView(context)
    }

    private fun initView(context: Context){
        mContext = context

         llTimesContainer = mView.findViewById(R.id.timesContainer)
        tvTotalValue = mView.findViewById(R.id.tvTotalValue)
        tvTotalValue.text = String.format(mContext.getString(R.string.total_hours_str),0,0)

    }

    fun setTimeDataList(list : List<TimeData>){
        mTimeDataList = list
        updateUI()
    }

    private fun updateUI(){

        for ((index,item) in mTimeDataList.withIndex()){
            minutes += item.getMinutesTotal()
            val workTimeView = WorkTimeRowView(context,item,this,index)
            workTimeRowViewList.add(workTimeView)
            timesContainer.addView(workTimeView)
        }
        tvTotalValue.text = getTextToShowTotalHour(minutes)
    }

    override fun onRemove(item: WorkTimeRowView) {
        minutes -= item.getMinutesTotal()
        updateTotal(minutes)
        workTimeRowViewList.remove(item)
        llTimesContainer.removeView(item)
        invalidate()
    }

    override fun onTimeChange() {
        minutes = 0
        for (item in workTimeRowViewList){
            minutes += item.getMinutesTotal()
        }
        tvTotalValue.text = getTextToShowTotalHour(minutes)
    }

    private fun updateTotal(minutes:Int){
        tvTotalValue.text = getTextToShowTotalHour(minutes)
    }

    private fun getTextToShowTotalHour(minutes:Int): String {
        var txtToShow = String.format(mContext.getString(R.string.total_hours_str),0,0)

        return if(minutes == 0){
            txtToShow
        } else {
            val hours = minutes / 60
            val min = minutes - (hours * 60)
            txtToShow = String.format(mContext.getString(R.string.total_hours_str), hours, min)
            txtToShow
        }
    }

    fun changeEditStatus(){
        if(!workTimeRowViewList.isNullOrEmpty()){
            editStatus = !editStatus
            for( item in workTimeRowViewList){
                item.setEditStatus()
            }
        }
    }

   /* fun addWorkTime(timeData : TimeData = TimeData("08:00-17:00")){
        minutes += timeData.getMinutesTotal()
        val workTimeView = WorkTimeRowView(context,timeData,this,workTimeRowViewList.size)
        workTimeView.setEditStatus(editStatus)
        workTimeRowViewList.add(workTimeView)
        timesContainer.addView(workTimeView)
    }*/

    fun getTimesData() : String {
        val timesData = mutableListOf<String>()
        val size = workTimeRowViewList.size
        if(size > 1){
            for(index in 0 until  size - 1 ){
                if((workTimeRowViewList[index].isValid() && workTimeRowViewList[index + 1].isValid()) &&
                    workTimeRowViewList[index].lessThan(workTimeRowViewList[index + 1]) ) {
                    timesData.add(workTimeRowViewList[index].getTimeData())
                    timesData.add(workTimeRowViewList[index + 1].getTimeData())
                } else {
                    return ""
                }
            }
            return timesData.joinToString("|")
        } else if(size <= 1) {
            return ""
        }
        return ""
    }
}