package com.cdsg.ficheaqui.ui.weekreport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdsg.ficheaqui.R

class WeekReportFragment: Fragment() {

  /*  private lateinit var weekReportViewModel by lazy {
        ViewModelProvider(this, LoginViewModelFactory(
            LoginUseCaseImpl(
                LoginRepo()
            )
        ))
            .get(LoginViewModel::class.java)
    }
*/
    private var listItems = mutableListOf(
        "Lunes de 09:00 a 17:00",
        "Martes de 09:00 a 17:00",
        "Miércoles de 09:00 a 17:00",
        "Jueves de 09:00 a 17:00",
        "Viernes de 09:00 a 17:00"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.week_report, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*val com.cdsg.ficheaqui.ui.worktime.adapter = DocumentsAdapter(context!!, listItems)
        listView.com.cdsg.ficheaqui.ui.worktime.adapter = com.cdsg.ficheaqui.ui.worktime.adapter*/
    }
}