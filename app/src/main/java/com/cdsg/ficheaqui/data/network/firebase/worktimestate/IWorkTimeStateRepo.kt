package com.cdsg.ficheaqui.data.network.firebase.worktimestate

import com.cdsg.ficheaqui.data.EnterData
import com.cdsg.ficheaqui.data.WorkTimeDataTimer
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateViewModel
import com.cdsg.ficheaqui.vo.Resource

interface IWorkTimeStateRepo {
    fun getWorkTimeState() : WorkTimeStateViewModel.WorkTimeState
    suspend fun saveEnterTimeAndPosition(value: EnterData) : Resource<WorkTimeDataTimer>
    fun setWorkTimeStatus(value : WorkTimeViewModel.WorkTimeStatus)
}