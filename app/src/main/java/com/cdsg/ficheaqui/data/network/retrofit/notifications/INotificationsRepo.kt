package com.cdsg.ficheaqui.data.network.retrofit.notifications

import com.cdsg.ficheaqui.data.Document
import com.cdsg.ficheaqui.vo.Resource

interface INotificationsRepo {

    suspend fun getNotificactions(): Resource<List<Document>>
   // suspend fun downloadDocument(file: String): Resource<Boolean>
}