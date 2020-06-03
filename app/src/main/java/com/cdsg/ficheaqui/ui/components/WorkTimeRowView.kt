package com.cdsg.ficheaqui.ui.components

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.TimeData

class WorkTimeRowView : LinearLayout {

    private lateinit var mContext: Context
    private var mView : View

    private lateinit var ivTimePickerEnd :ImageView
    private lateinit var ivTimePickerStart :ImageView

    private lateinit var tvFromValue : TextView
    private lateinit var tvToValue : TextView

    private lateinit var timeData: TimeData
    private var mListener: WorkTimeRowViewAct
    private var mTag: Int = -1
    private var editStatus: Boolean = false

    constructor(context: Context, timeData: TimeData, listener: WorkTimeRowViewAct, tag: Int) : super(context) {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mView = inflater.inflate(R.layout.working_time_row_view, this, true)
        mListener = listener
        mTag = tag
        initView(context,timeData)
    }

    fun initView(context: Context, data:TimeData){
        mContext = context
        timeData = data

        ivTimePickerStart =  mView.findViewById(R.id.ivTimePickerStart)
        ivTimePickerEnd =  mView.findViewById(R.id.ivTimePickerEnd)

        tvToValue =  mView.findViewById(R.id.tvToValue)
        tvFromValue =  mView.findViewById(R.id.tvFromValue)

        tvFromValue.text = timeData.getStartTime()
        tvToValue.text = timeData.getEndTime()

        ivTimePickerStart.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                mContext,
                R.style.DialogTheme,
                OnTimeSetListener { _ , hourOfDay, minute ->
                    timeData.setStartHourAndMinute(hourOfDay,minute)
                    tvFromValue.text = timeData.getStartTime()
                    mListener.onTimeChange()
                },
                timeData.getStartHour(),
                timeData.getStartMinute(),
                false
            )
            timePickerDialog.show()
        }

        ivTimePickerEnd.setOnClickListener {
            val timePickerDialog = TimePickerDialog(
                mContext,
                R.style.DialogTheme,
                OnTimeSetListener { _ , hourOfDay, minute ->
                    timeData.setEndHourAndMinute(hourOfDay,minute)
                    tvToValue.text = timeData.getEndTime()
                    mListener.onTimeChange()
                },
                timeData.getEndHour(),
                timeData.getEndMinute(),
                false
            )
            timePickerDialog.show()
        }
    }

    interface WorkTimeRowViewAct {
        fun onRemove(item: WorkTimeRowView)
        fun onTimeChange()
    }

    fun getMinutesTotal() = timeData.getMinutesTotal()

    fun setEditStatus(){
        editStatus = !editStatus

        if(editStatus) {
            ivTimePickerStart.visibility = View.VISIBLE
            ivTimePickerEnd.visibility = View.VISIBLE
        } else {
            ivTimePickerStart.visibility = View.GONE
            ivTimePickerEnd.visibility = View.GONE
        }
    }

    fun getTimeData() = timeData.getTimeData()

    fun getTimeDataObj() = timeData

    fun isValid() = timeData.isValid()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkTimeRowView

        if (timeData != other.timeData) return false
        if (mTag != other.mTag) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timeData.hashCode()
        result = 31 * result + mTag
        return result
    }
}