package com.freshworks.southwest.data

import com.freshworks.sdk.data.WebWidgetConfig

data class TagsConfig(
    val tags: String,
    @WebWidgetConfig.FilterType
    val filterType: String
)