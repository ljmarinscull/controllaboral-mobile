package com.cdsg.ficheaqui.ui.docdetails

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdsg.ficheaqui.data.Document
import com.cdsg.ficheaqui.domain.usescases.docdetails.IDocDetailsUseCase
import com.cdsg.ficheaqui.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DocDetailsViewModel(private val usecase: IDocDetailsUseCase) : ViewModel() {

    private val _uploadSignStatus = MutableLiveData<Resource<String>>()
    val uploadSignStatus : LiveData<Resource<String>>?
        get() = _uploadSignStatus

    private val _downLoadStatus = MutableLiveData<Resource<Boolean>>()
    val downLoadStatus : LiveData<Resource<Boolean>>?
        get() = _downLoadStatus

    private val _rejectSigningOrderStatus = MutableLiveData<Resource<Boolean>>()
    val rejectSigningOrderStatus : LiveData<Resource<Boolean>>?
        get() = _rejectSigningOrderStatus

    private var uri : Uri = Uri.EMPTY
    private var document: Document? = null

    fun onUploadSignedFile(){
        viewModelScope.launch(Dispatchers.Main) {
            _uploadSignStatus.value = Resource.Loading()
            try {
                var result : Resource<String>? = null
                withContext(Dispatchers.IO){
                    val filename = "${document?.id}_signed.pdf"
                    result = usecase.uploadSignedDocument(filename, uri)
                }
                _uploadSignStatus.value = result!!
            } catch (e: Exception) {
                _uploadSignStatus.value = Resource.Failure(e)
            }
        }
    }

    fun downLoadFile(tempFile: File) {
        viewModelScope.launch(Dispatchers.Main) {
            _downLoadStatus.value = Resource.Loading()
            try {
                var result : Resource<Boolean>? = null
                withContext(Dispatchers.IO){
                    result = usecase.downLoadFile("${document?.id}.pdf", tempFile)
                }
                _downLoadStatus.value = result!!
            } catch (e: Exception) {
                _downLoadStatus.value = Resource.Failure(e)
            }
        }
    }

    fun onRejectToSignDoc(){
        viewModelScope.launch(Dispatchers.Main) {
            _rejectSigningOrderStatus.value = Resource.Loading()
            try {
                var result : Resource<Boolean>? = null
                withContext(Dispatchers.IO){
                    result = usecase.backSigningOrder(document?.id!!)
                }
                _rejectSigningOrderStatus.value = result!!
            } catch (e: Exception) {
                _rejectSigningOrderStatus.value = Resource.Failure(e)
            }
        }
    }

    fun setFileUri(value: Uri) {
       uri = value
    }

    fun setDocument(value: Document) {
        document = value
    }

}