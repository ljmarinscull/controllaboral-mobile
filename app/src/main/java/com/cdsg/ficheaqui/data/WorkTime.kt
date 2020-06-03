package com.cdsg.ficheaqui.data

import com.google.firebase.firestore.GeoPoint

class WorkTime (val worked: Boolean, val geo: GeoPoint, val worktimes: ArrayList<String>) {
    constructor(geo: GeoPoint,  worktimes: ArrayList<String>) : this(true, geo, worktimes)
}