# Freshworks Android SDK

Modern messaging software that your sales and customer engagement teams will love [FreshworksSDK](https://www.freshworks.com).

## Installation

Freshworks SDK is distributed through Maven Central. To use it you need to add the following Gradle dependency to the build.gradle file of your android app module (NOT the root file).

```
dependencies {
  implementation "com.freshworks.sdk:core:{sdkVersion}"
}
```

## Documentation
### Initializing the SDK
In your application class' `onCreate()` function, initialize the SDK.
```kotlin
FreshworksSDK.initialize(this, SDKConfig(
        appId = "YOUR_APP_ID",
        appKey = "YOUR_APP_KEY",
        domain = "YOUR_APP_DOMAIN",
        widgetUrl = "YOUR_WIDGET_URL",
        languageCode = "YOUR_LANGUAGE_CODE")
      ) { 
            //Call back when SDK initialization is complete. 
        }
```
You can find the `APP_ID`, `APP_KEY`, `APP_DOMAIN` from the `Admin settings` page of your CRM portal.

You can find the `WIDGET_URL` from the `Web widget` page of your CRM portal. Copy the value in the `src` attribute as shown in the below screenshot. It starts with `\\` and ends with `.js`.

<img width="1667" alt="Screenshot 2023-07-06 at 3 15 11 PM" src="https://github.com/freshworks/freshworks-android-sdk/assets/53250947/4aa3903c-3d29-4c9f-98d5-6d6659bbcd02">

<img width="1675" alt="Screenshot 2023-07-06 at 3 38 51 PM" src="https://github.com/freshworks/freshworks-android-sdk/assets/53250947/e008b091-d2d0-4f24-9da1-643697d4f7e3">

## User API's & Events

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
FreshworksSDK.resetUser(onSuccess: () -> Unit, onFailure: () -> Unit))
```

### Get user details

If you ever want to get the properties of the user, you can do so with the following.

*Note: This is an asynchronous call.*
```kotlin
FreshworksSDK.getUser { user ->
    Log.d("FreshworksSDK", user.toString())
}
```

### User creation Event
A user record will be created in our system when your user initiates their first chat. You will receive a notification when this happens, which you can listen to via a broadcast listener.
```kotlin
LocalBroadcastManager.getInstance(this).registerReceiver(object: BroadcastReceiver() {
          override fun onReceive(context: Context?, intent: Intent?) {
              when (intent?.action) {
                  EventID.USER_CREATED -> {
                      Log.d("FreshworksSDK", "User created")
                  }
              }
          }

      }, IntentFilter().apply {
           addAction(EventID.USER_CREATED)
      })
```

If you have set an external identifier for a user, and the user initiates a chat, the SDK will provide a "Restore ID" to you, so you can restore this particular user later.
To listen to the "Restore ID" generated event, refer to the below example.
```kotlin
LocalBroadcastManager.getInstance(this).registerReceiver(object: BroadcastReceiver() {
          override fun onReceive(context: Context?, intent: Intent?) {
              when (intent?.action) {
                    EventID.USER_CREATED -> {
                        FreshworksSDK.getUser { user ->
                        Log.d("FreshworksSDK", "Restore ID : ${user.restoreId}")
                        Log.d("FreshworksSDK" : "User Alias: ${user.alias}")
                    }
                }
              }
          }

      }, IntentFilter().apply {
           addAction(EventID.USER_CREATED)
      })
```

## Open support screens

### All Conversations
To display a list of topics/conversations, use the example below. You can also filter the topics based on tags using the second parameter.
```kotlin
FreshworksSDK.showConversations(
                        context,
                        listOf("topicTag") //The tags that will be used to filter the topics.
)
```
### Parallel Conversation

In Freshchat, it used to be difficult to have multiple conversations on the same topic. This was particularly challenging when dealing with Payment Support in an e-commerce setting, where many transactions had to be managed under one topic. But now, we have a solution called Parallel Conversation, which allows multiple conversation threads on a single topic. To use this feature, simply provide a unique conversationReferenceId with the FreshworksSDK. Support representatives can help customers set up unique conversationReferenceIds, which will enable seamless topic-specific conversations. To view the conversations, customers can use the API showConversation.

```
 FreshworksSDK.showConversation(
                        context,
                        conversationReferenceId, //The external reference ID of the conversation to be opened.
                        topicName   //The topic name in which the conversation is present.
                    )
```

### FAQs
To show the FAQs screen, use the below example. Optionally, you can pass the second parameter to filter the FAQs based on articles, or categories.
```kotlin
FreshworksSDK.showFAQs(context, faqTags {
      tags = arrayListOf("faqTag")
      filterType = WebWidgetConfig.FilterType.ARTICLE //Or WebWidgetConfig.FilterType.CATEGORY
  })
```


## Push notifications
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


## Multi-Language Support (Locale Change)
FreshworksSDK provides multilanguage support. The list of all supported languages and their keys are provided in the table below.

| Language Code | Language                     |
|---------------|------------------------------|
| ar            | Arabic Language (RTL)       |
| en            | English                      |
| ca            | Catalan                      |
| zh-HANS       | Chinese (Simplified)         |
| zh-HANT       | Chinese (Traditional)        |
| cs            | Czech                        |
| da            | Danish                       |
| nl            | Dutch                        |
| de            | German                       |
| et            | Estonian                     |
| fi            | Finnish                      |
| fr            | French                       |
| hu            | Hungarian                    |
| id            | Indonesian                   |
| it            | Italian                      |
| ko            | Korean                       |
| lv            | Latvian                      |
| nb            | Norwegian                    |
| pl            | Polish                       |
| pt            | Portuguese                   |
| pt-BR         | Portuguese - Brazil          |
| ro            | Romanian                     |
| ru            | Russian                      |
| sk            | Slovak                       |
| sl            | Slovenian                    |
| es            | Spanish                      |
| es-LA         | Spanish - Latin America      |
| sv            | Swedish                      |
| th            | Thai                         |
| tr            | Turkish                      |
| uk            | Ukrainian                    |
| vi            | Vietnamese                   |


The language is selected based on priority and is explained below.

When using the freshworksSDK, the language that is given the highest priority is the one that is specified during initialization or when the changeWidgetLanguage public API is called. To set the language, use the "locale" key in the code.

```
FreshworksSDK.initialize(this, SDKConfig(
        appId = "YOUR_APP_ID",
        appKey = "YOUR_APP_KEY",
        domain = "YOUR_APP_DOMAIN",
        widgetUrl = "YOUR_WIDGET_URL",
        languageCode = "YOUR_LANGUAGE_CODE")
      ) { 
            //Call back when SDK initialization is complete. 
        }
```

If you need to change the language of the widget after the SDK has been initialized, use the following API call to change the widget language at runtime.

```
FreshworksSDK.setLocale(languageCode)
```
Default Language: The lowest priority is the Default Language which is set in your Freshchat account. This will be the primary language that is specified on your Freshchat Account.
User locale change


To change the language of the Freshchat widget dynamically based on user selection, such as through a language dropdown, you need to set the locale when the dropdown is changed. To specify the language in this case, use the code provided below. For a better understanding, please refer to the sample code provided.

```
FreshworksSDK.setUserLocale(languageCode)
```

## JWT Authentication
Enabling user authentication using JSON Web Token:
Freshchat uses JSON Web Token (JWT) to ensure that only authenticated users can start a conversation with you through the Freshchat messenger. Whenever there is an event related to the user's authentication status or the messenger, Freshchat sends a callback to notify you along with the user's authentication status.

Step 1: Create the JWT without UUID using the public & private keys.

Step 2: Initiate the SDK with the above JWT.

```
FreshworksSDK.initialize(this, SDKConfig(
        appId = "YOUR_APP_ID",
        appKey = "YOUR_APP_KEY",
        domain = "YOUR_APP_DOMAIN",
        widgetUrl = "YOUR_WIDGET_URL",
        jwtAuthToken = "YOUR_JWT_AUTH_TOKEN")
      ) { 
            //Call back when SDK initialization is complete. 
        }
```

Note: If your account is JWT enabled, it is mandatory to pass JWT while SDK initialization.

Step 3: Please make sure to set up your broadcast receiver to receive the user state event in your application class. You can refer to the code snippet provided below for guidance.

```
LocalBroadcastManager.getInstance(this).registerReceiver(object: BroadcastReceiver() {
          override fun onReceive(context: Context?, intent: Intent?) {
              when (intent?.action) {
                  EventID.USER_STATE -> {
                      Log.d("FreshworksSDK", intent.getStringExtra(EventID.USER_STATE))
                  }
              }
          }

      }, IntentFilter().apply {
           addAction(EventID.USER_STATE)
      })

```


Step 4: Once the user states update is received, if the user state is NOT_LOADED, UNLOADED, NOT_CREATED, NOT_AUTHENTICATED, then fetch the UUID from 'FreshworksSDK.getUUID()'

```
   LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    EventID.USER_STATE -> {
                        when (userState) {
                            UserState.UNDEFINED -> {
                                // Do nothing
                            }

                            UserState.NOT_LOADED,
                            UserState.UNLOADED,
                            UserState.NOT_CREATED,
                            UserState.NOT_AUTHENTICATED -> {
                                FreshworksSDK.getUUID()
                            }

                            else -> {
                                Log.d("FreshworksSDK", "User state is $userState")
                            }
                        }
                    }

                    EventID.GET_UUID_SUCCESS -> {
                        Log.d("FreshworksSDK", intent.getStringExtra(EventID.GET_UUID_SUCCESS))
                    } 
                }
            }

        }, IntentFilter().apply {
            addAction(EventID.USER_STATE)
        })
```

Step 5: Create a valid JWT using the UUID received from Step 4. Then, update the user using the API Call provided below.

```
   FreshworksSDK.updateUser(
         jwt = "UPDATED_JWT" //Valid JWT created using the UUID
   )
```

Note: The above API is also responsible for updating the user details. While creating the JWT, the details which need to be updated should be added to the payload.

## Support
support@freshchat.com

[Support Portal](https://support.freshchat.com/en/support/home)
