package com.freshworks.southwest.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IdentifyUserData(
    val userAlias: String,
    var externalId: String,
    val restoreId: String
) : Parcelable