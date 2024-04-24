package com.freshworks.southwest.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.sdk.FreshworksSDK
import com.freshworks.sdk.data.FaqOptions
import com.freshworks.sdk.data.FilterType
import com.freshworks.sdk.events.FreshchatEvent.UserState
import com.freshworks.southwest.R
import com.freshworks.southwest.SouthWestApp
import com.freshworks.southwest.components.buttons.ButtonText
import com.freshworks.southwest.components.dialogs.ChangeLanguageDialog
import com.freshworks.southwest.components.dialogs.IdentifyUserDialog
import com.freshworks.southwest.components.dialogs.LoadAccountForm
import com.freshworks.southwest.components.dialogs.LocalizationSupportPropertiesDialog
import com.freshworks.southwest.components.dialogs.ProgressBarDialog
import com.freshworks.southwest.components.dialogs.PropertiesStringResource
import com.freshworks.southwest.components.dialogs.TagsDialog
import com.freshworks.southwest.components.dialogs.TextFieldDialog
import com.freshworks.southwest.components.dialogs.UpdateAuthTokenDialog
import com.freshworks.southwest.components.dialogs.UpdateCustomPropertiesDialog
import com.freshworks.southwest.components.dialogs.UpdateUserDialog
import com.freshworks.southwest.data.DataStore
import com.freshworks.southwest.data.DataStore.saveToastEventsState
import com.freshworks.southwest.data.DialogConfig
import com.freshworks.southwest.data.IdentifyUserData
import com.freshworks.southwest.ui.theme.SouthWestTheme
import com.freshworks.southwest.utils.toMap
import com.freshworks.southwest.utils.toast
import java.util.Locale

const val TAG = "SettingsActivity"
const val DELAY_MILLIS = 10000L

class SettingsActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SouthWestTheme {
                Scaffold(
                    topBar = {
                        Row(modifier = Modifier.padding(end = 40.dp)) {
                            TopAppBar(title = {
                                Text(
                                    text = stringResource(id = R.string.developer_settings),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }, navigationIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back_arrow),
                                    contentDescription = stringResource(id = R.string.back),
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            onBackPressed()
                                        },
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            })
                            ToastEventsSwitch()
                        }
                    },
                ) { paddingValues -> SetContent(paddingValues) }
            }
        }
    }

    @Composable
    private fun SetContent(paddingValues: PaddingValues) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ShowConversations()
            OpenParallelConversation()
            ButtonText(textId = R.string.show_faqs) {
                FreshworksSDK.showFAQs(this@SettingsActivity, FaqOptions.faqOptions {})
            }
            ConversationAndFaqTags()
            IdentifyUser()
            UpdateProperties()
            ResetUser()
            UpdateJWT()
            ChangeWidgetLanguage()
            ChangeUserLanguage()
            LogUserEvent()
            LoadAccount()
            UpdateBotVariables()
            UpdateCustomConversationProperties()
            UpdateStaticTextLocalization()
            DismissFreshChatViews()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowConversations() {
        val unreadCount = rememberSaveable { mutableStateOf("0") }
        (applicationContext as SouthWestApp).unreadCount.observeAsState().value?.let {
            unreadCount.value = it.toString()
        }
        BadgedBox(
            badge = {
                Badge(modifier = Modifier.padding(top = 10.dp)) {
                    Text(text = unreadCount.value)
                }
            },
            modifier = Modifier.padding(8.dp)
        ) {
            ButtonText(textId = R.string.show_conversations) {
                FreshworksSDK.showConversations(this@SettingsActivity)
            }
        }
    }

    @Composable
    fun OpenParallelConversation() {
        val openDialog = rememberSaveable { mutableStateOf(false) }
        if (openDialog.value) {
            TextFieldDialog(
                config = DialogConfig(),
                textField1 = Pair(R.string.conv_ref_id, DataStore.getConvRefId()),
                textField2 = Pair(R.string.topic_name, DataStore.getConvTopic()),
                onConfirmed = {
                    openDialog.value = false
                    DataStore.setConvRefId(it.field1)
                    DataStore.setConvTopic(it.field2)
                    FreshworksSDK.showConversation(
                        this@SettingsActivity,
                        it.field1,
                        it.field2
                    )
                }, onDismissed = {
                    openDialog.value = false
                }
            )
        }
        ButtonText(textId = R.string.parallel_conversations) {
            openDialog.value = true
        }
    }

    @Composable
    fun ConversationAndFaqTags() {
        val openDialog = rememberSaveable { mutableStateOf(false) }
        if (openDialog.value) {
            TagsDialog(
                tagsValue = DataStore.getTags(),
                filterTypeValue = DataStore.getFilterType(),
                onConfirmed = { config ->
                    DataStore.setTags(config.tags)
                    DataStore.setFilterType(config.filterType)
                    val tagsList = config.tags.split(",").map { it.trim() }
                    if (config.filterType == FilterType.CONVERSATION) {
                        FreshworksSDK.showConversations(this, tagsList)
                    } else {
                        FreshworksSDK.showFAQs(
                            this@SettingsActivity,
                            FaqOptions(tags = tagsList, config.filterType)
                        )
                    }
                    openDialog.value = false
                }
            ) {
                openDialog.value = false
            }
        }
        ButtonText(textId = R.string.conversation_faq_tags) {
            openDialog.value = true
        }
    }

    @Composable
    private fun UpdateBotVariables() {
        val openUpdateCustomBotVariables = rememberSaveable { mutableStateOf(false) }
        if (openUpdateCustomBotVariables.value) {
            UpdateCustomPropertiesDialog(
                propertiesStringResource = PropertiesStringResource(
                    dialogTitle = R.string.update_custom_bot_variables,
                    propertiesFormDescription = R.string.bot_variables_description,
                    labelId = R.string.bot_variables,
                ),
                customProperties = DataStore.getBotVariables(),
                onConfirmed = { botVariables ->
                    DataStore.setBotVariablesValues(botVariables)
                    FreshworksSDK.showConversations(this@SettingsActivity)
                    FreshworksSDK.setBotVariables(botVariables)
                    openUpdateCustomBotVariables.value = false
                },
                onDismissed = {
                    openUpdateCustomBotVariables.value = false
                }
            )
        }
        ButtonText(textId = R.string.update_custom_bot_variables) {
            openUpdateCustomBotVariables.value = true
        }
    }

    @Composable
    private fun UpdateStaticTextLocalization() {
        val openStaticLocalizationSupportDialog = rememberSaveable { mutableStateOf(false) }
        if (openStaticLocalizationSupportDialog.value) {
            LocalizationSupportPropertiesDialog(
                dialogTitle = R.string.update_localization_support,
                headerProperties = DataStore.getLocalizationConfigHeaderProperties(),
                contentProperties = DataStore.getLocalizationConfigContentProperties(),
                onConfirmed = { headerProperties, contentProperties ->
                    DataStore.setLocalizationConfigHeaderProperties(headerProperties)
                    DataStore.setLocalizationConfigContentProperties(contentProperties)
                    FreshworksSDK.setConfigProperties(
                        headerProperties,
                        contentProperties
                    )
                    openStaticLocalizationSupportDialog.value = false
                },
                onDismissed = {
                    openStaticLocalizationSupportDialog.value = false
                }
            )
        }
        ButtonText(textId = R.string.update_localization_support) {
            openStaticLocalizationSupportDialog.value = true
        }
    }

    @Composable
    private fun UpdateCustomConversationProperties() {
        val openCustomConversationPropertiesDialog = rememberSaveable { mutableStateOf(false) }
        if (openCustomConversationPropertiesDialog.value) {
            UpdateCustomPropertiesDialog(
                propertiesStringResource = PropertiesStringResource(
                    dialogTitle = R.string.update_custom_conversation_properties,
                    propertiesFormDescription = R.string.conversation_properties_description,
                    labelId = R.string.conversation_properties,
                ),
                customProperties = DataStore.getConversationProperties(),
                onConfirmed = { conversationProperties ->
                    DataStore.setConversationProperties(conversationProperties)
                    FreshworksSDK.setConversationProperties(conversationProperties)
                    openCustomConversationPropertiesDialog.value = false
                },
                onDismissed = {
                    openCustomConversationPropertiesDialog.value = false
                }
            )
        }
        ButtonText(textId = R.string.update_custom_conversation_properties) {
            openCustomConversationPropertiesDialog.value = true
        }
    }

    @Composable
    fun IdentifyUser() {
        val openDialog = rememberSaveable { mutableStateOf(false) }
        val notFound = stringResource(id = R.string.not_found).uppercase(Locale.ROOT)
        if (openDialog.value) {
            IdentifyUserDialog(
                config = DialogConfig(
                    title = R.string.identify_user,
                    positiveText = R.string.identify_show
                ),
                userData = IdentifyUserData(
                    userAlias = if (DataStore.getUserAlias() == "") {
                        notFound
                    } else {
                        DataStore.getUserAlias()
                    },
                    externalId = DataStore.getExternalId(),
                    restoreId = DataStore.getRestoreId()
                ),
                onConfirmed = {
                    openDialog.value = false
                    DataStore.setExternalId(it.externalId)
                    DataStore.setRestoreId(it.restoreId)
                    FreshworksSDK.identifyUser(
                        externalId = it.externalId,
                        restoreId = it.restoreId
                    )
                    FreshworksSDK.showConversations(this@SettingsActivity)
                }, onDismissed = {
                    openDialog.value = false
                }
            )
        }
        ButtonText(textId = R.string.identify_user) {
            openDialog.value = true
        }
    }

    @Composable
    fun UpdateProperties() {
        val openDialog = rememberSaveable { mutableStateOf(false) }
        if (openDialog.value) {
            UpdateUserDialog(userData = DataStore.getUser(), onConfirmed = { user ->
                DataStore.setUser(user)
                FreshworksSDK.setUserDetails(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = user.email,
                    phone = user.phoneNumber,
                    phoneCountry = user.phoneCountry,
                    properties = user.properties.toMap()
                )
                openDialog.value = false
            }) {
                openDialog.value = false
            }
        }
        ButtonText(textId = R.string.update_properties) {
            openDialog.value = true
        }
    }

    @Composable
    fun ResetUser() {
        val resetDialog = rememberSaveable { mutableStateOf(false) }
        ButtonText(textId = R.string.reset_user) {
            resetDialog.value = true
            FreshworksSDK.resetUser({
                resetDialog.value = false
                toast(it)
                FreshworksSDK.setUserDetails()
                DataStore.clearUser()
            }) {
                resetDialog.value = false
                toast(it)
            }
        }
        if (resetDialog.value) {
            ProgressBarDialog(R.string.resetting_user)
        }
    }

    @Composable
    fun UpdateJWT() {
        val fetching = stringResource(id = R.string.fetching)
        val notFound = stringResource(id = R.string.not_found).uppercase(Locale.ROOT)
        val userState = rememberSaveable { mutableStateOf(fetching) }
        val freshChatUUID = rememberSaveable { mutableStateOf(notFound) }
        val openDialog = rememberSaveable { mutableStateOf(false) }
        val account = DataStore.getSelectedAccount()

        (application as SouthWestApp).uuid.observeAsState().value?.let { uuid ->
            freshChatUUID.value = uuid
        }
        (application as SouthWestApp).userState.observeAsState().value?.let { state ->
            userState.value = state
            handleUserStates(state)
        }
        Button(
            onClick = {
                openDialog.value = true
            }, shape = ShapeDefaults.Large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.update_jwt),
                fontWeight = FontWeight.SemiBold
            )
        }
        if (openDialog.value) {
            UpdateAuthTokenDialog(
                userState = userState.value,
                uuid = freshChatUUID.value,
                authTokenValue = account.jwtAuthToken,
                onValueChange = {
                    DataStore.setSelectedAccount(account = account.copy(jwtAuthToken = it))
                    FreshworksSDK.updateUser(it)
                    openDialog.value = false
                }
            ) {
                openDialog.value = false
            }
        }
    }

    @Composable
    fun ChangeWidgetLanguage() {
        val openChangeLanguageDialog = rememberSaveable { mutableStateOf(false) }
        if (openChangeLanguageDialog.value) {
            ChangeLanguageDialog(
                title = R.string.widget_language,
                currentLanguageCode = DataStore.getWidgetLocale(),
                onConfirmed = { language ->
                    DataStore.saveWidgetLocale(language)
                    FreshworksSDK.setLocale(language)
                    openChangeLanguageDialog.value = false
                },
                onDismissed = { openChangeLanguageDialog.value = false }
            )
        }
        ButtonText(textId = R.string.change_widget_language) {
            openChangeLanguageDialog.value = true
        }
    }

    @Composable
    fun ChangeUserLanguage() {
        val openChangeLanguageDialog = rememberSaveable { mutableStateOf(false) }
        if (openChangeLanguageDialog.value) {
            ChangeLanguageDialog(
                title = R.string.user_language,
                currentLanguageCode = DataStore.getUserLocale(),
                onConfirmed = { language ->
                    DataStore.saveUserLocale(language)
                    FreshworksSDK.setUserLocale(language)
                    openChangeLanguageDialog.value = false
                },
                onDismissed = { openChangeLanguageDialog.value = false }
            )
        }
        ButtonText(textId = R.string.change_user_language) {
            openChangeLanguageDialog.value = true
        }
    }

    @Composable
    fun LoadAccount() {
        val openLoadAccountDialog = rememberSaveable { mutableStateOf(false) }
        if (openLoadAccountDialog.value) {
            LoadAccountForm(DataStore.getSelectedAccount(), {
                openLoadAccountDialog.value = false
                DataStore.setSelectedAccount(it)
                FreshworksSDK.resetUser({}) {}
                FreshworksSDK.initialize(
                    this@SettingsActivity,
                    it.copy(
                        languageCode = DataStore.getWidgetLocale(),
                        headerProperties = DataStore.getLocalizationConfigHeaderProperties(),
                        contentProperties = DataStore.getLocalizationConfigContentProperties()
                    )
                ) {
                    Log.i(SOUTH_WEST, "Resetting current user and loading new SDK configuration")
                }
            }) {
                openLoadAccountDialog.value = false
            }
        }
        ButtonText(textId = R.string.load_account) {
            openLoadAccountDialog.value = true
        }
    }

    @Composable
    private fun LogUserEvent() {
        val logUserEventDialog = rememberSaveable { mutableStateOf(false) }
        if (logUserEventDialog.value) {
            TextFieldDialog(
                config = DialogConfig(
                    title = R.string.log_user_event,
                    positiveText = R.string.send,
                    showDescription = true
                ),
                textField1 = Pair(R.string.event_name, DataStore.getInboundEventName()),
                textField2 = Pair(R.string.event_value, DataStore.getInboundEventData()),
                onConfirmed = {
                    logUserEventDialog.value = false
                    if (it.field1.isEmpty()) {
                        toast(getString(R.string.inbound_event_invalid_input))
                    } else {
                        val message = getString(R.string.event_sent, it.field1)
                        DataStore.saveInboundEventName(it.field1)
                        DataStore.saveInboundEventData(it.field2)
                        if (it.field2.isEmpty()) {
                            FreshworksSDK.trackEvent(it.field1, emptyMap())
                            toast(message)
                        } else {
                            val eventValue = it.field2.toMap()
                            if (eventValue.isNotEmpty()) {
                                FreshworksSDK.trackEvent(it.field1, it.field2.toMap())
                                toast(message)
                            } else {
                                toast(getString(R.string.inbound_event_invalid_input))
                            }
                        }
                    }
                },
                onDismissed = { logUserEventDialog.value = false }
            )
        }
        ButtonText(textId = R.string.log_user_event) { logUserEventDialog.value = true }
    }

    private fun handleUserStates(userState: String) {
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
                Log.d(TAG, "User state is $userState")
            }
        }
    }

    @Composable
    fun ToastEventsSwitch() {
        var toastEventsState by remember { mutableStateOf(DataStore.getToastEventsState()) }
        Switch(
            checked = toastEventsState,
            onCheckedChange = {
                toastEventsState = it
                saveToastEventsState(it)
            }, modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
        )
    }

    @Composable
    fun DismissFreshChatViews() {
        ButtonText(textId = R.string.dismiss_freshChat_views) {
            Handler().postDelayed({
                Log.d(TAG, "Dismissing WebWidget")
                FreshworksSDK.dismissFreshchatViews()
            }, DELAY_MILLIS)
            FreshworksSDK.showConversations(this@SettingsActivity)
        }
    }
}