package com.freshworks.southwest

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.freshworks.sdk.FreshworksSDK
import com.freshworks.sdk.R.drawable.ic_launcher_background
import com.freshworks.sdk.core.FreshchatWebViewListener
import com.freshworks.sdk.data.SDKConfig
import com.freshworks.sdk.events.EventID
import com.freshworks.sdk.notification.NotificationConfig
import com.freshworks.sdk.utils.changeLocale
import com.freshworks.southwest.data.DataStore
import com.freshworks.southwest.data.DataStore.sharedPreferences
import com.freshworks.southwest.ui.activity.SOUTH_WEST

const val TAG = "SouthWest"

class SouthWestApp : Application() {

    private val _unreadCount: MutableLiveData<Int> = MutableLiveData()
    val unreadCount: LiveData<Int> = _unreadCount

    private val _userState: MutableLiveData<String> = MutableLiveData()
    val userState: LiveData<String> = _userState

    private val _uUID: MutableLiveData<String> = MutableLiveData()
    val uuid: LiveData<String> = _uUID

    val customLocale = "ar"

    private val myReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                EventID.UNREAD_COUNT -> {
                    _unreadCount.value = intent.getIntExtra(EventID.UNREAD_COUNT, 0)
                }

                EventID.USER_CREATED -> {
                    FreshworksSDK.getUser { user ->
                        DataStore.setUserAlias(user.alias)
                        DataStore.setRestoreId(user.restoreId ?: "")
                        Log.d(TAG, "Restore ID : ${user.restoreId}")
                    }
                }

                EventID.USER_STATE -> {
                    _userState.value = intent.getStringExtra(EventID.USER_STATE)
                }

                EventID.GET_UUID_SUCCESS -> {
                    _uUID.value = intent.getStringExtra(EventID.GET_UUID_SUCCESS)
                    Log.d("UUID DEMO APP", _uUID.value ?: "")
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = this.getSharedPreferences(SOUTH_WEST, Context.MODE_PRIVATE)
        initializeSDK(
            DataStore.getSelectedAccount().copy(languageCode = DataStore.getWidgetLocale()),
            this
        )
        FreshworksSDK.setNotificationConfig(
            NotificationConfig(
                true,
                ic_launcher_background,
                importance = NotificationManager.IMPORTANCE_HIGH
            )
        )
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver,
            IntentFilter().apply {
                addAction(EventID.USER_CREATED)
                addAction(EventID.UNREAD_COUNT)
                addAction(EventID.USER_STATE)
                addAction(EventID.GET_UUID_SUCCESS)
            }
        )
        FreshworksSDK.setWebViewListener(webViewListener)
    }

    private val webViewListener: FreshchatWebViewListener = object : FreshchatWebViewListener {
        override fun onLocaleChangedByWebView() {
            if (this@SouthWestApp == null) {
                return
            }
            changeLocale(this@SouthWestApp, customLocale)
        }
    }

    companion object {
        fun initializeSDK(sdkConfig: SDKConfig, context: Context) {
            FreshworksSDK.initialize(context, sdkConfig) {
                Log.d("Demo App", "SDK initialization complete")
            }
        }
    }
}
