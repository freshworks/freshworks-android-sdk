package com.freshworks.southwest.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneCountry: String = "",
    val phoneNumber: String = "",
    val properties: String = ""
) : Parcelable