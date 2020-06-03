package com.cdsg.ficheaqui.domain.usescases.docdetails

import android.net.Uri
import com.cdsg.ficheaqui.data.network.firebase.docdetails.IDocDetailsRepo
import com.cdsg.ficheaqui.vo.Resource
import java.io.File

class DocDetailsUseCaseImpl(private val repo: IDocDetailsRepo) :  IDocDetailsUseCase {

    override suspend fun uploadSignedDocument(filename: String, file: Uri) = repo.uploadSignedDocument(filename,file)
    override suspend fun downLoadFile(filePath: String, tempFile: File) = repo.downLoadFile(filePath,tempFile)
    override suspend fun backSigningOrder(filename: String) = repo.backSigningOrder(filename)
}