package com.cdsg.ficheaqui.domain.usescases.main

import com.cdsg.ficheaqui.data.network.firebase.main.IMainRepo
import com.cdsg.ficheaqui.ui.main.MainViewModel
import com.cdsg.ficheaqui.ui.worktimestate.WorkTimeStateViewModel

class MainUseCaseImpl(private val repo: IMainRepo) : IMainUseCase {

    override fun setSecondsRemaining(seconds: Long) {
        repo.setSecondsRemaining(seconds)
    }

    override fun getSecondsRemaining() : Long {
       return repo.getSecondsRemaining()
    }

    override fun getTodayWorkTimes(): String {
        return repo.getTodayWorkTimes()
    }

    override fun setTodayWorkTimes(time: String) {
        repo.setTodayWorkTimes(time)
    }

    override fun setEnterTime(time: String) {
        repo.setEnterTime(time)
    }

    override fun getEnterTime(): String {
        return repo.getEnterTime()
    }

    override fun saveWorkTimeState(value: WorkTimeStateViewModel.WorkTimeState) {
        repo.saveWorkTimeState(value)
    }

    override fun getWorkTimeState(): WorkTimeStateViewModel.WorkTimeState {
          return  repo.getWorkTimeState()
    }

    override fun setTimerState(value: MainViewModel.TimerState) = repo.setTimerState(value)

    override fun getTimerState() = repo.getTimerState()

    override fun setCurrentTime(time: Long) {
        repo.setCurrentTime(time)
    }

    override fun getAlarmSetTime(): Long {
        return repo.getAlarmSetTime()
    }
}