package com.cdsg.ficheaqui.data.network.retrofit.docdetails

import com.cdsg.ficheaqui.vo.Resource


class DocDetailsRepoImpl : IDocDetailsRepo  {

    override suspend fun uploadSignedDocument(filename: String, file: String): Resource<Boolean>? {
        return Resource.Success(true)
    }

    override suspend fun backSigningOrder(filename: String): Resource<Boolean>? {
        return Resource.Success(true)
    }
}