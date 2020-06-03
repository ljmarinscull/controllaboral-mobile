package com.cdsg.ficheaqui.domain.usescases.notifications

import com.cdsg.ficheaqui.data.Document
import com.cdsg.ficheaqui.vo.Resource

interface INotificationsUseCase {

    suspend fun getNotificactions(): Resource<List<Document>>
   // suspend fun downloadDocument(file: String): Resource<Boolean>
}