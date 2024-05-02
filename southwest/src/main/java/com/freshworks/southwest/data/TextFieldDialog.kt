package com.freshworks.southwest.data

import androidx.annotation.StringRes
import com.freshworks.southwest.R

data class TextFieldDialog(
    val field1: String,
    val field2: String
)

data class DialogConfig(
    @StringRes val title: Int = R.string.open_Conversation,
    @StringRes val positiveText: Int = R.string.open,
    val showDescription: Boolean = false
)