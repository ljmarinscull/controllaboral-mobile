package com.cdsg.ficheaqui.data

import com.google.firebase.firestore.GeoPoint

data class EnterData(val geo: GeoPoint, val enterTime: String)