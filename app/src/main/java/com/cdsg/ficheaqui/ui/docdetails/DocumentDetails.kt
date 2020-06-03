package com.cdsg.ficheaqui.ui.docdetails

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cdsg.ficheaqui.R
import com.cdsg.ficheaqui.data.Document
import com.cdsg.ficheaqui.data.network.firebase.docdetails.DocDetailsRepoImpl
import com.cdsg.ficheaqui.databinding.DocumentDetailsBinding
import com.cdsg.ficheaqui.domain.usescases.docdetails.DocDetailsUseCaseImpl
import com.cdsg.ficheaqui.utils.hideProgress
import com.cdsg.ficheaqui.utils.isOnline
import com.cdsg.ficheaqui.utils.showProgress
import com.cdsg.ficheaqui.utils.toast
import com.cdsg.ficheaqui.vo.Resource
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.document_details.*
import java.io.File

class DocumentDetails : Fragment() {

    private val docDetailsViewModel by lazy {
        ViewModelProvider(this, DocDetailsViewModelFactory(
            DocDetailsUseCaseImpl(
                DocDetailsRepoImpl()
            )
        )
        ).get(DocDetailsViewModel::class.java)
    }

    private var document: Document? = null
    private val args: DocumentDetailsArgs by navArgs()
    private lateinit var binding : DocumentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.document_details, container, false)
        val view = binding.root
        //here data must be an instance of the class MarsDataProvider
        binding.viewModel = docDetailsViewModel
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        docDetailsViewModel.setDocument(args.doc)

        mbSignDoc.setOnClickListener {
            if (isOnline()) {
                checkStorePermission()
            }
            else{
                toast("No esta conectado a Internet. Revise su conexión.",1)
            }
        }

        mbSeeDoc.setOnClickListener{
            if (isOnline()) {
                val localFile = File.createTempFile(args.doc.name, "pdf")
                docDetailsViewModel.downLoadFile(localFile)
            }
            else {
                toast("No esta conectado a Internet. Revise su conexión.",1)
            }
        }


        docDetailsViewModel.uploadSignStatus?.observe(viewLifecycleOwner, Observer { value ->
            when(value){
                is Resource.Loading -> {
                    progressBar.showProgress()
                    toast("Subiendo archivo firmado.",0)
                }
                is Resource.Success -> {
                    progressBar.hideProgress()
                    if(value.data.isNotEmpty()) {
                        Log.e("FicheAqui:fileUrl", value.data)
                        toast("Se ha subido el archivo firmado satisfactoriamente.",0)
                        val action = DocumentDetailsDirections
                            .actionDocdetailsToNotifications()
                        findNavController().navigate(action)
                    } else {
                        toast("Ha ocurrido un error al intentar subir el archivo.",0)
                    }
                }
                is Resource.Failure -> {
                    progressBar.hideProgress()
                    toast("Ha ocurrido un error al intentar subir el archivo.",0)
                }
            }
        })

        docDetailsViewModel.downLoadStatus?.observe(viewLifecycleOwner, Observer { value ->
            when(value){
                is Resource.Loading -> {
                    progressBar.showProgress()
                    toast("Descargando archivo...",0)
                }
                is Resource.Success -> {
                    progressBar.hideProgress()
                    if(value.data) {
                       toast("Archivo descargado satisfactoriamente.",0)
                        val action = DocumentDetailsDirections
                            .actionDocdetailsToNotifications()
                        findNavController().navigate(action)
                    } else {
                        toast("Ha ocurrido un error descargando el archivo.",0)
                    }
                }
                is Resource.Failure -> {
                    progressBar.hideProgress()
                    toast("Ha ocurrido un error descargando el archivo.",0)
                }
            }
        })

        docDetailsViewModel.rejectSigningOrderStatus?.observe(viewLifecycleOwner, Observer { value ->
            when(value){
                is Resource.Loading -> {
                    progressBar.showProgress()
                }
                is Resource.Success -> {
                    progressBar.hideProgress()
                    if(value.data) {
                        toast("Se ha enviado su desacuerdo satisfactoriamente.",0)
                        val action = DocumentDetailsDirections
                            .actionDocdetailsToNotifications()
                        findNavController().navigate(action)

                    } else {
                        toast("Ha ocurrido un error al notificar su desacuerdo.",0)
                    }
                }
                is Resource.Failure -> {
                    progressBar.hideProgress()
                    toast("Ha ocurrido un error al notificar su desacuerdo.",0)
                }
            }
        })
    }

    private fun checkStorePermission() {
        Dexter.withActivity(activity!!)
            .withPermissions(listOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if(report.areAllPermissionsGranted()){
                        fileChooser()
                    }
                }
                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>?, token: PermissionToken? ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun fileChooser() {
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,1)
    }

    /* private fun getExtension( uri: Uri) : String? {
         val cr = activity!!.contentResolver
         val mimeTypeMap = MimeTypeMap.getSingleton()
         return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri))
     }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            docDetailsViewModel.setFileUri(data.data!!)
            docDetailsViewModel.onUploadSignedFile()
        }
    }

}