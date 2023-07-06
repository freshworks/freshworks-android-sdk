package com.freshworks.hybridsdkdemo

import android.util.Log
import com.freshworks.sdk.FreshworksSDK
import com.google.firebase.messaging.FirebaseMessagingService


/**
 * Created by Robin Rex G on 06/07/23.
 */
class MyMessagingSerivce: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        Log.d("HybridSDKDemo", "Updating FCM token to SDK")
        FreshworksSDK.setPushRegistrationToken(p0)
    }

}