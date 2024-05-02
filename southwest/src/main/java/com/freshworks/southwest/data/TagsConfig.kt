package com.freshworks.southwest.data

import com.freshworks.sdk.data.FilterType

data class TagsConfig(
    val tags: String,
    @FilterType
    val filterType: String
)