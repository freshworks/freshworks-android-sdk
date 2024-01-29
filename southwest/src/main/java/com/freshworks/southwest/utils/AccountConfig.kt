package com.freshworks.southwest.utils

import com.freshworks.sdk.data.SDKConfig

object AccountConfig {
    val defaultAccount = SDKConfig(
        appId = "YOUR_APP_ID",
        appKey = "YOUR_APP_KEY",
        domain = "YOUR_APP_DOMAIN",
        widgetUrl = "YOUR_WIDGET_URL"
    )
}