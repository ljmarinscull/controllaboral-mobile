package com.cdsg.ficheaqui.data

data class NoWorked(val worked: Boolean, val whyNot: String) {
    constructor(whyNot: String) : this(false,whyNot)
}