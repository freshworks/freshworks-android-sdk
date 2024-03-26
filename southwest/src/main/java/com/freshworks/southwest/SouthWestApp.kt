package com.freshworks.southwest

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.freshworks.sdk.FreshworksSDK
import com.freshworks.sdk.R.drawable.ic_launcher_background
import com.freshworks.sdk.core.FreshchatWebViewListener
import com.freshworks.sdk.data.SDKConfig
import com.freshworks.sdk.events.EventID
import com.freshworks.sdk.notification.NotificationConfig
import com.freshworks.sdk.utils.FreshchatLinkHandler
import com.freshworks.sdk.utils.changeLocale
import com.freshworks.southwest.data.DataStore
import com.freshworks.southwest.data.DataStore.sharedPreferences
import com.freshworks.southwest.ui.activity.SOUTH_WEST
import com.freshworks.southwest.utils.toast

const val TAG = "SouthWest"

class SouthWestApp : Application() {

    private val _unreadCount: MutableLiveData<Int> = MutableLiveData()
    val unreadCount: LiveData<Int> = _unreadCount

    private val _userState: MutableLiveData<String> = MutableLiveData()
    val userState: LiveData<String> = _userState

    private val _uUID: MutableLiveData<String> = MutableLiveData()
    val uuid: LiveData<String> = _uUID

    val customLocale = "ar"

    val myReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                EventID.UNREAD_COUNT -> {
                    _unreadCount.value = intent.getIntExtra(EventID.UNREAD_COUNT, 0)
                    context?.toast("$UNREAD_COUNT : ${_unreadCount.value}")
                }

                EventID.USER_CREATED -> {
                    FreshworksSDK.getUser { user ->
                        DataStore.setUserAlias(user.alias)
                        DataStore.setRestoreId(user.restoreId ?: "")
                        context?.toast("$RESTORE_ID : ${user.restoreId}")
                        Log.d(TAG, "$RESTORE_ID : ${user.restoreId}")
                    }
                }

                EventID.USER_STATE -> {
                    _userState.value = intent.getStringExtra(EventID.USER_STATE)
                    context?.toast("$USER_STATE : ${_userState.value}")
                }

                EventID.GET_UUID_SUCCESS -> {
                    _uUID.value = intent.getStringExtra(EventID.GET_UUID_SUCCESS)
                    Log.d("UUID DEMO APP", _uUID.value ?: "")
                }

                EventID.MESSAGE_SENT -> {
                    context?.toast(MESSAGE_SENT)
                    Log.d(TAG + MESSAGE_SENT, "${intent.extras}")
                }

                EventID.MESSAGE_RECEIVED -> {
                    context?.toast(MESSAGE_RECEIVED)
                    Log.d(TAG + MESSAGE_RECEIVED, "${intent.extras}")
                }

                EventID.CSAT_RECEIVED -> {
                    context?.toast(CSAT_RECEIVED)
                    Log.d(TAG + CSAT_RECEIVED, "${intent.extras}")
                }

                EventID.CSAT_UPDATED -> {
                    context?.toast(CSAT_UPDATED)
                    Log.d(TAG + CSAT_UPDATED, "${intent.extras}")
                }

                EventID.DOWNLOAD_FILE -> {
                    context?.toast(DOWNLOAD_FILE)
                    Log.d(TAG + DOWNLOAD_FILE, "${intent.extras}")
                }

                EventID.USER_CLEARED -> {
                    context?.toast(USER_CLEARED)
                    Log.d(TAG, USER_CLEARED)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = this.getSharedPreferences(SOUTH_WEST, Context.MODE_PRIVATE)
        initializeSDK(
            DataStore.getSelectedAccount().copy(
                languageCode = DataStore.getWidgetLocale(),
                headerProperties = DataStore.getLocalizationConfigHeaderProperties(),
                contentProperties = DataStore.getLocalizationConfigContentProperties()
            ),
            this
        )
        FreshworksSDK.setNotificationConfig(
            NotificationConfig(
                true,
                ic_launcher_background,
                importance = NotificationManager.IMPORTANCE_HIGH
            )
        )
        FreshworksSDK.setWebViewListener(webViewListener)
        FreshworksSDK.setLinkHandler(linkHandler)
    }

    private val webViewListener: FreshchatWebViewListener = object : FreshchatWebViewListener {
        override fun onLocaleChangedByWebView() {
            if (this@SouthWestApp == null) {
                return
            }
            changeLocale(this@SouthWestApp, customLocale)
        }
    }

    private val linkHandler: FreshchatLinkHandler = object : FreshchatLinkHandler {
        override fun handleLink(url: String?) {
            Toast.makeText(
                this@SouthWestApp,
                getString(R.string.link_clicked, url),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {

        private const val UNREAD_COUNT = "UNREAD COUNT"
        private const val RESTORE_ID = "RESTORE ID"
        private const val MESSAGE_SENT = "MESSAGE SENT"
        private const val MESSAGE_RECEIVED = "MESSAGE RECEIVED"
        private const val CSAT_RECEIVED = "CSAT RECEIVED"
        private const val CSAT_UPDATED = "CSAT UPDATED"
        private const val USER_STATE = "USER STATE"
        private const val USER_CLEARED = "USER CLEARED"
        private const val DOWNLOAD_FILE = "DOWNLOAD FILE"

        fun initializeSDK(sdkConfig: SDKConfig, context: Context) {
            FreshworksSDK.initialize(context, sdkConfig) {
                Log.d("Demo App", "SDK initialization complete")
            }
        }
    }
}
