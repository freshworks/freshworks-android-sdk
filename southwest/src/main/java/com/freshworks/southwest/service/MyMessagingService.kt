package com.freshworks.southwest.service

import com.freshworks.sdk.FreshworksSDK
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyMessagingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        println("Token : $p0")
        FreshworksSDK.setPushRegistrationToken(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        println("Firebase Message : ${p0.data}")
        if (FreshworksSDK.isFreshworksSDKNotification(p0.data)) {
            FreshworksSDK.handleFCMNotification(p0.data)
        }
    }
}