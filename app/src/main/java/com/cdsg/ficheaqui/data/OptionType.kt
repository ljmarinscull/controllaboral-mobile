package com.cdsg.ficheaqui.data

import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.utils.FOR_AUSENT
import com.cdsg.ficheaqui.utils.FOR_HOLLIDAY
import com.cdsg.ficheaqui.utils.FOR_VACATIONS

enum class OptionType {
    WORKTIME{
        override fun getTextToShow() = "Corregir Horario"
        override fun getDBId() = ""
        override fun getIcon() = R.drawable.ic_worktime
    },
    AUSENT{
        override fun getTextToShow() = "Ausencia"
        override fun getDBId() = FOR_AUSENT
        override fun getIcon() = R.drawable.ic_ausent
    },
    HOLLIDAY{
        override fun getTextToShow() = "Festivo"
        override fun getDBId() = FOR_HOLLIDAY
        override fun getIcon() = R.drawable.ic_holiday
    },
    VACATIONS {
        override fun getTextToShow() = "Vacaciones"
        override fun getDBId() = FOR_VACATIONS
        override fun getIcon() = R.drawable.ic_vacations
    };

    abstract fun getTextToShow():String
    abstract fun getDBId():String
    abstract fun getIcon():Int
}