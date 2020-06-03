package com.cdsg.ficheaqui.data.network.firebase.docdetails

import android.net.Uri
import com.cdsg.ficheaqui.data.UploadFileResponse
import com.cdsg.ficheaqui.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File


class DocDetailsRepoImpl : IDocDetailsRepo  {

    override suspend fun uploadSignedDocument(filename: String, file: Uri): Resource<String>? {
        val email = FirebaseAuth.getInstance().currentUser?.email!!
        val ref = FirebaseStorage.getInstance().getReference("/controllaboral/${email}/signed")
        val child = ref.child(filename)
        val result = child.putFile(file).await()
        val url = result.metadata?.reference?.downloadUrl?.await() // Enviar a la BD
        return Resource.Success(url?.path!!)
    }

    override suspend fun downLoadFile(filePath: String, tempFile: File): Resource<Boolean>? {
        val email = FirebaseAuth.getInstance().currentUser?.email!!
        val ref = FirebaseStorage.getInstance().getReference("/controllaboral/${email}/pending")
        val child = ref.child(filePath)
        child.getFile(tempFile).await()
        return Resource.Success(true)
    }

    override suspend fun backSigningOrder(filename: String): Resource<Boolean>? {
        return Resource.Success(true)
    }
}