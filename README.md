# Freshworks Android SDK


"Modern messaging software that your sales and customer engagement teams will love [FreshworksSDK](https://www.freshworks.com)."

## Installation 

Freshworks Android SDK can be integrated into your Android application with the following steps.

1. Copy the following three files into the `libs` folder of your app module.
2. Add these as dependencies in your app module-level  build.gradle file
   
```groovy 
implementation files('libs/backend-0.0.1.aar')
implementation files('libs/JSAnnotations-0.0.1.jar')
implementation files('libs/widgetCore-0.0.1.aar')
```
3. Add the dependencies required by the SDK in your app's build.gradle file.
```groovy
//Dagger
implementation "com.google.dagger:dagger-android:2.46"
implementation "com.google.dagger:dagger-android-support:2.46"

//Retrofit
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.3.0'

//Webkit
implementation "androidx.webkit:webkit:1.5.0"

//Gson
implementation 'com.google.code.gson:gson:2.10.1'
```

## Documentation
### Initializing the SDK
In your application class' `onCreate()` function, initialize the SDK.
```kotlin
FreshworksSDK.initialize(this, SDKConfig(
            "YOUR_APP_ID",
            "YOUR_APP_KEY",
            "YOUR_APP_DOMAIN",
            "YOUR_WIDGET_URL"
        )) { 
            //Call back when SDK initialization is complete. 
        }
```
You can find the `APP_ID`, `APP_KEY`, `APP_DOMAIN` from the `Admin settings` page of your CRM portal.

You can find the `WIDGET_URL` from the `Web widget` page of your CRM portal. Copy the value in the `src` attribute as shown in the below screenshot. It starts with `\\` and ends with `.js`.

<img width="1667" alt="Screenshot 2023-07-06 at 3 15 11 PM" src="https://github.com/freshworks/freshworks-android-sdk/assets/53250947/4aa3903c-3d29-4c9f-98d5-6d6659bbcd02">

<img width="1675" alt="Screenshot 2023-07-06 at 3 38 51 PM" src="https://github.com/freshworks/freshworks-android-sdk/assets/53250947/e008b091-d2d0-4f24-9da1-643697d4f7e3">

### Identifying/Restoring a user

To be able to identify a user, you can set an identifier (called "External ID") with the `identifyUser` API. Call this API after initializing the SDK, and before the user starts the chat. This external ID is unique to your system and can be any alphanumeric.
```kotlin
FreshworksSDK.identifyUser(<External ID>)
```
Until you set an identifier, all users that are created are "anonymous", meaning if they log out of your app or if they log in from a different device, they won't be able to continue the chat.

To be able to restore the conversation of a user across sessions, or across devices, we provide something called a "Restore ID". Whenever a user gets created in our system, and if there is an identifier set, we generate the "Restore ID" and provide it to you (Refer to [this](#restore-id-generation) section). You can use this restore ID to restore an existing user and their conversations.

You can use the same API that you used before to restore an existing user. If you pass an "External ID", along with a "Restore ID" that was provided by the SDK, the user matching this combination will be restored along with their conversation history.
```kotlin
FreshworksSDK.identifyUser(<External ID>, <Restore ID>)
```

### Updating user details

You can update a user's details such as first and last name, email, and phone. You can also add your own properties in the "properites" parameter as a map. These  custom properties will have to be first created in your CRM account.
```kotlin
FreshworksSDK.setUserDetails(
                  firstName = "alphanumeric",
                  lastName = "alphanumeric",
                  email = "example@example.com",
                  phone = "1234567890",
                  phoneCountry = "+1",
                  properties = mapOf("cf_custom_field" to "Test")
              )
```

### Reset a user

You would want to reset the user and their conversations when they log out of the applications. To do this, you can make use of the following API. Make sure to call this during logout, to properly unsubscribe from receiving further messages or notifications.
```kotlin
FreshworksSDK.resetUser()
```

### Get user details

If you ever want to get the properties of the user, you can do so with the following. 

*Note: This is an asynchronous call.*
```kotlin
FreshworksSDK.getUser { user ->
    Log.d(FreshworksSDK.TAG, user.toString())
}
```

### Events
#### User creation 
A user record will be created in our system when your user first initiates a chat. When a user gets created in our system, you will be notified, which you can listen via a broadcast listener.
```kotlin
LocalBroadcastManager.getInstance(this).registerReceiver(object: BroadcastReceiver() {
          override fun onReceive(context: Context?, intent: Intent?) {
              when (intent?.action) {
                  FCEvents.USER_CREATED -> {
                      Log.d("HybridSDK", "User created")
                  }
              }
          }

      }, IntentFilter().apply {
          addAction(FCEvents.USER_CREATED)
      })
```

#### Restore ID generation
If you have set an external identifier for a user, and the user initiates a chat, the SDK will provide a "Restore ID" to you, so you can restore this particular user later.
To listen to the "Restore ID" generated event, refer to the below example.
```kotlin
LocalBroadcastManager.getInstance(this).registerReceiver(object: BroadcastReceiver() {
          override fun onReceive(context: Context?, intent: Intent?) {
              when (intent?.action) {
                  FCEvents.RESTORE_ID_GENERATED -> {
                      FreshworksSDK.getUser { user ->
                          Log.d(TAG, "Restore ID : ${user.restoreId}")
                      }
                  }
              }
          }

      }, IntentFilter().apply {
          addAction(FCEvents.RESTORE_ID_GENERATED)
      })
```

### Open support screens
To show the list of topics/conversations, use the below example. Optionally, you can pass the second parameter to filter the topics based on tags. 
```kotlin
FreshworksSDK.showConversations(this@DemoActivity, listOf("robin new"))
```

To show the FAQs screen, use the below example. Optionally, you can pass the second parameter to filter the FAQs based on articles, or categories.
```kotlin
FreshworksSDK.showFAQs(this, faqTags {
      tags = arrayListOf("test1")
      filterType = WebWidgetConfig.FilterType.ARTICLE //Or WebWidgetConfig.FilterType.CATEGORY
  })
```

### Push notifications
To be able to receive messages when not active in the app or in the chat screen, you can set up push notifications. Your application should have FCM integreted, and should have an implementation of `FirebaseMessagingService`.
To set up the configuration for notifications, 

```kotlin
FreshworksSDK.setNotificationConfig(NotificationConfig(true, //Sound enabled 
 R.drawable.ic_launcher_background, //Notification icon
 importance = android.app.NotificationManager.IMPORTANCE_HIGH))
```

In your `onNewToken` method, pass on the device token received from FCM to the SDK.

```kotlin
 override fun onNewToken(token: String) {
      super.onNewToken(token)
      FreshworksSDK.setPushRegistrationToken(token)
  }
```

And then in your `onMessageReceived` method, you can pass on the payload to the SDK, if it the notification is from the SDK.

```kotlin
override fun onMessageReceived(message: RemoteMessage) {
    super.onMessageReceived(message)
    
    if(FreshworksSDK.isFreshworksSDKNotification(message.data)) {
        FreshworksSDK.handleFCMNotification(message.data)
    }
}
```
