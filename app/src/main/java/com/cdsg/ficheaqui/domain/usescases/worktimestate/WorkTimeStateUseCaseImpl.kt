package com.cdsg.ficheaqui.domain.usescases.worktimestate

import com.cdsg.ficheaqui.data.EnterData
import com.cdsg.ficheaqui.data.network.firebase.worktimestate.IWorkTimeStateRepo
import com.cdsg.ficheaqui.ui.worktime.WorkTimeViewModel
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateViewModel

class WorkTimeStateUseCaseImpl(private val repo: IWorkTimeStateRepo) : IWorkTimeStateUseCase {

    override fun getWorkTimeState():
            WorkTimeStateViewModel.WorkTimeState = repo.getWorkTimeState()

    override suspend fun saveEnterTimeAndPosition(value: EnterData)
            = repo.saveEnterTimeAndPosition(value)

    override fun setWorkTimeStatus(value: WorkTimeViewModel.WorkTimeStatus)
            = repo.setWorkTimeStatus(value)
}