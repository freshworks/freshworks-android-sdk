package com.freshworks.southwest.utils

import com.freshworks.sdk.data.SDKConfig

object AccountConfig {
    val defaultAccount = SDKConfig(
        appId = "Your-app-ID",
        appKey = "Your-app-Key",
        domain = "Your-domain",
        widgetUrl = "Your-widget-URL",
        widgetId = "Your-widget-ID"
    )
}