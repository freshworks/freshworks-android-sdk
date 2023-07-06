package com.freshworks.hybridsdkdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.freshworks.sdk.FreshworksSDK
import com.freshworks.sdk.data.SDKConfig
import com.freshworks.sdk.events.FCEvents

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FreshworksSDK.initialize(this, SDKConfig(
            "accf0b5a-2034-43e1-875a-de9c5e4f3955",
            "3a7a632a-32f0-44db-93b2-113c8867817e",
            "mobiletestingstaging-org-844f4ac14e9078016883932.freshpori.com",
            "//fm-staging-us-app-cdnjs.s3.amazonaws.com/crm/7575962/6419591.js"
        )) {
            //Call back when SDK initialization is complete.
        }

        register()
    }

    private fun register() {
        LocalBroadcastManager.getInstance(this).registerReceiver(object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    FCEvents.USER_CREATED -> {
                        Log.d("HybridSDK", "User created")
                    }

                    FCEvents.RESTORE_ID_GENERATED -> {
                        FreshworksSDK.getUser {
                            Log.d("HybridSDK", "Restore ID : ${it.restoreId}")
                        }
                    }
                }
            }

        }, IntentFilter().apply {
            addAction(FCEvents.USER_CREATED)
            addAction(FCEvents.RESTORE_ID_GENERATED)
        })
    }
}