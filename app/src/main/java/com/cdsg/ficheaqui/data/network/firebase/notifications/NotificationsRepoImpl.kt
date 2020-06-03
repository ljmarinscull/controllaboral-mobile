package com.cdsg.ficheaqui.data.network.firebase.notifications

import com.cdsg.ficheaqui.data.Document
import com.cdsg.ficheaqui.utils.DOCUMENTS
import com.cdsg.ficheaqui.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class NotificationsRepoImpl : INotificationsRepo {

    override suspend fun getNotificactions(): Resource<List<Document>> {
        val email = FirebaseAuth.getInstance().currentUser?.email!!

        val documentList = FirebaseFirestore.getInstance().collection(DOCUMENTS).document(email).get().await()
        val list = mutableListOf<Document>()

        for ((k, v) in documentList.data?.iterator()!!) {
            @Suppress("UNCHECKED_CAST")
            val result = v as HashMap<String,String>
            val doc = Document(k,result["name"],result["url"])
            list.add(doc)
        }

        return Resource.Success(list)
    }
}