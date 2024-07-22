package com.freshworks.southwest

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.freshworks.backend.model.User
import com.freshworks.sdk.FreshchatUserInteractionListener
import com.freshworks.sdk.FreshworksSDK
import com.freshworks.sdk.core.FreshchatWebViewListener
import com.freshworks.sdk.data.SDKConfig
import com.freshworks.sdk.events.EventID
import com.freshworks.sdk.notification.NotificationConfig
import com.freshworks.sdk.utils.changeLocale
import com.freshworks.southwest.data.DataStore
import com.freshworks.southwest.data.DataStore.sharedPreferences
import com.freshworks.southwest.ui.activity.SOUTH_WEST
import com.freshworks.southwest.utils.logEvent
import com.freshworks.southwest.utils.toast

const val TAG = "SouthWest"

class SouthWestApp : Application(), FreshchatUserInteractionListener {

    private val _unreadCount: MutableLiveData<Int> = MutableLiveData()
    val unreadCount: LiveData<Int> = _unreadCount

    private val _userState: MutableLiveData<String> = MutableLiveData()
    val userState: LiveData<String> = _userState

    private val _uUID: MutableLiveData<String> = MutableLiveData()
    val uuid: LiveData<String> = _uUID

    val customLocale = "ar"
    override fun onCreate() {
        super.onCreate()
        sharedPreferences = this.getSharedPreferences(SOUTH_WEST, Context.MODE_PRIVATE)
        setUpFreshworksSDK()
        setWebViewListener()
        registerBroadcastReceiver()
        FreshworksSDK.setFreshchatUserInteractionListener(this)
    }

    private fun setUpFreshworksSDK() {
        initializeSDK(
            DataStore.getSelectedAccount().copy(
                languageCode = DataStore.getWidgetLocale(),
                headerProperties = DataStore.getLocalizationConfigHeaderProperties(),
                contentProperties = DataStore.getLocalizationConfigContentProperties()
            ),
            this
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FreshworksSDK.setNotificationConfig(
                NotificationConfig(
                    soundEnabled = true,
                    smallIconResId = R.drawable.ic_southwest_notification,
                    largeIconResId = R.drawable.ic_southwest_notification,
                    importance = NotificationManager.IMPORTANCE_HIGH
                )
            )
        } else {
            FreshworksSDK.setNotificationConfig(
                NotificationConfig(
                    soundEnabled = true,
                    smallIconResId = R.drawable.ic_southwest_notification,
                    largeIconResId = R.drawable.ic_southwest_notification,
                    importance = NotificationCompat.PRIORITY_HIGH
                )
            )
        }
    }

    private fun setWebViewListener() {
        val webViewListener: FreshchatWebViewListener = object : FreshchatWebViewListener {
            override fun onLocaleChangedByWebView() {
                changeLocale(this@SouthWestApp, customLocale)
            }
        }
        FreshworksSDK.setWebViewListener(webViewListener)
    }

    private fun registerBroadcastReceiver() {
        val myReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    EventID.UNREAD_COUNT -> {
                        _unreadCount.value = intent.getIntExtra(EventID.UNREAD_COUNT, 0)
                        context?.logEvent(
                            UNREAD_COUNT,
                            _unreadCount.value.toString(),
                            true
                        )
                    }

                    EventID.RESTORE_ID_GENERATED -> {
                        val restoreId = intent.getStringExtra(EventID.RESTORE_ID_GENERATED)
                        DataStore.setRestoreId(restoreId ?: "")
                        context?.logEvent(RESTORE_ID, restoreId ?: "", true)
                    }

                    EventID.GET_UUID_SUCCESS -> {
                        _uUID.value = intent.getStringExtra(EventID.GET_UUID_SUCCESS)
                        context?.logEvent(GET_UUID_SUCCESS, _uUID.value ?: "", true)
                    }

                    EventID.MESSAGE_SENT -> {
                        context?.logEvent(MESSAGE_SENT, "${intent.extras}")
                    }

                    EventID.MESSAGE_RECEIVED -> {
                        context?.logEvent(MESSAGE_RECEIVED, "${intent.extras}")
                    }

                    EventID.CSAT_RECEIVED -> {
                        context?.logEvent(CSAT_RECEIVED, "${intent.extras}")
                    }

                    EventID.CSAT_UPDATED -> {
                        context?.logEvent(CSAT_UPDATED, "${intent.extras}")
                    }

                    EventID.DOWNLOAD_FILE -> {
                        context?.logEvent(DOWNLOAD_FILE, "${intent.extras}")
                    }

                    else -> userEvents(intent, context)
                }
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver,
            IntentFilter().apply {
                addAction(EventID.USER_CREATED)
                addAction(EventID.RESTORE_ID_GENERATED)
                addAction(EventID.UNREAD_COUNT)
                addAction(EventID.USER_STATE)
                addAction(EventID.GET_UUID_SUCCESS)
                addAction(EventID.MESSAGE_SENT)
                addAction(EventID.MESSAGE_RECEIVED)
                addAction(EventID.CSAT_RECEIVED)
                addAction(EventID.CSAT_UPDATED)
                addAction(EventID.DOWNLOAD_FILE)
                addAction(EventID.USER_CLEARED)
            }
        )
    }

    @Suppress("DEPRECATION")
    fun userEvents(intent: Intent?, context: Context?) {
        when (intent?.action) {
            EventID.USER_CREATED -> {
                val user: User? =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.extras?.getParcelable(EventID.USER_CREATED, User::class.java)
                    } else {
                        intent.extras?.getParcelable(EventID.USER_CREATED) as? User
                    }
                user?.let {
                    DataStore.setUserAlias(it.alias)
                    if (DataStore.getRestoreId().isEmpty()) {
                        DataStore.setRestoreId(it.restoreId ?: "")
                    }
                    context?.logEvent(USER_CREATED, user.alias, true)
                }
            }

            EventID.USER_STATE -> {
                _userState.value = intent.getStringExtra(EventID.USER_STATE)
                context?.logEvent(USER_STATE, _userState.value ?: "", true)
            }

            EventID.USER_CLEARED -> {
                context?.logEvent(USER_CLEARED, "")
            }
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
        private const val GET_UUID_SUCCESS = "GET UUID SUCCESS"
        private const val USER_CREATED = "USER_CREATED"

        fun initializeSDK(sdkConfig: SDKConfig, context: Context) {
            FreshworksSDK.initialize(context, sdkConfig) {
                Log.d(TAG, "SDK initialization complete")
            }
        }
    }

    override fun onUserInteraction() {
        Log.d(TAG, "onUserInteraction() called")
        if (DataStore.getUserActionState()) {
            toast("User Interacted!!")
        }
    }
}
