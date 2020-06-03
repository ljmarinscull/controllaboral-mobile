package com.cdsg.ficheaqui.domain.usescases.notifications

import com.cdsg.ficheaqui.data.Document
import com.cdsg.ficheaqui.data.network.firebase.notifications.INotificationsRepo
import com.cdsg.ficheaqui.domain.usescases.notifications.INotificationsUseCase
import com.cdsg.ficheaqui.vo.Resource


class NotificationsUseCaseImpl(private val repo: INotificationsRepo) : INotificationsUseCase{
    
    override suspend fun getNotificactions(): Resource<List<Document>> = repo.getNotificactions()
}