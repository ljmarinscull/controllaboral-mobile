package com.cdsg.ficheaqui.data.network.firebase.docdetails

import android.net.Uri
import com.cdsg.ficheaqui.vo.Resource
import java.io.File

interface IDocDetailsRepo  {

    suspend fun uploadSignedDocument(filename: String, file: Uri): Resource<String>?
    suspend fun downLoadFile(filePath: String, tempFile: File): Resource<Boolean>?
    suspend fun backSigningOrder(filename: String): Resource<Boolean>?
}