package com.cdsg.ficheaqui.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.Document
import com.cdsg.ficheaqui.data.network.firebase.notifications.NotificationsRepoImpl
import com.cdsg.ficheaqui.databinding.DocumentsInboxBinding
import com.cdsg.ficheaqui.domain.usescases.notifications.NotificationsUseCaseImpl
import com.cdsg.ficheaqui.ui.main.MainActivity
import com.cdsg.ficheaqui.ui.adapters.DocumentsAdapter
import com.cdsg.ficheaqui.utils.hideProgress
import com.cdsg.ficheaqui.utils.showProgress
import com.cdsg.ficheaqui.utils.toast
import com.cdsg.ficheaqui.vo.Resource
import kotlinx.android.synthetic.main.documents_inbox.*

class NotificationsFragment : Fragment() {

    private val notificationsViewModel by lazy {
        ViewModelProvider(this, NotificationsViewModelFactory(
            NotificationsUseCaseImpl(
                NotificationsRepoImpl()
            )
        )
        ).get(NotificationsViewModel::class.java)
    }

    private lateinit var binding : DocumentsInboxBinding
    private lateinit var mAdapter : DocumentsAdapter

    private var listItems = mutableListOf(
        Document("document_semanal","Documento semanal","gs://caribbean-dsg.appspot.com/controllaboral/ljmarinscull@gmail.com/pending/document_semanal.pdf"),
        Document("document_mensual","Documento mensual","gs://caribbean-dsg.appspot.com/controllaboral/ljmarinscull@gmail.com/pending/document_mensual.pdf"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.documents_inbox, container, false)
        val view = binding.root

        binding.viewModel = notificationsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).getNetWorkStatus().observe(viewLifecycleOwner, Observer {

            if(it){
                notificationsViewModel.documentList.observe(viewLifecycleOwner, Observer { value ->
                    when(value){
                        is Resource.Loading -> {
                            progressBar.showProgress()
                        }
                        is Resource.Success -> {
                            progressBar.hideProgress()
                            mAdapter = if(value.data.isEmpty()) {
                                DocumentsAdapter(context!!, listItems)
                            } else {
                                DocumentsAdapter(context!!, listItems)
                            }
                            listView.adapter = mAdapter
                        }
                        is Resource.Failure -> {
                            progressBar.hideProgress()
                            Log.e("Resource.Failure",value.exception.toString())
                            toast("Ha ocurrido un error.",0)
                        }
                    }
                } )

                listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ ->
                    documentItemSelected(adapterView.getItemAtPosition(i) as Document)
                }} else {
                (activity as MainActivity).toast("No esta conectado a Internet. Revise su conexión.",1)
            }
        })
    }

    private fun documentItemSelected(document: Document) {
        val action = NotificationsFragmentDirections
            .actionNotificationsToDocdetails(document)
        findNavController().navigate(action)
    }
}


