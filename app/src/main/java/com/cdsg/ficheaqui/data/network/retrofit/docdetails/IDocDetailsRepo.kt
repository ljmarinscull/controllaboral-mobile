package com.cdsg.ficheaqui.data.network.retrofit.docdetails

import com.cdsg.ficheaqui.vo.Resource

interface IDocDetailsRepo  {
    suspend fun uploadSignedDocument(filename: String, file: String): Resource<Boolean>?

    suspend fun backSigningOrder(filename: String): Resource<Boolean>?
}