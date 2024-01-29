package com.freshworks.southwest.data

import android.content.SharedPreferences
import com.freshworks.sdk.data.SDKConfig
import com.freshworks.southwest.utils.AccountConfig
import com.google.gson.Gson

const val USER_NAME = "USER_NAME"
const val CONV_REF_ID = "CONV_REF_ID"
const val CONV_TOPIC_NAME = "CONV_TOPIC_NAME"
const val SELECTED_ACCOUNT = "SELECTED_ACCOUNT"
const val USER_ALIAS = "USER_ALIAS"
const val EXTERNAL_ID = "EXTERNAL_ID"
const val RESTORE_ID = "RESTORE_ID"
const val TAGS = "TAGS"
const val FILTER_TYPE = "FILTER_TYPE"
const val USER = "USER"
const val USER_LOCALE = "USER_LOCALE"
const val WIDGET_LOCALE = "WIDGET_LOCALE"

object DataStore {

    lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    fun getUserName(): String {
        return sharedPreferences.getString(USER_NAME, null) ?: ""
    }

    fun setUserName(userName: String) {
        sharedPreferences.edit().putString(USER_NAME, userName).apply()
    }

    fun setConvRefId(convRefId: String) {
        sharedPreferences.edit().putString(CONV_REF_ID, convRefId).apply()
    }

    fun getConvRefId(): String {
        return sharedPreferences.getString(CONV_REF_ID, null) ?: ""
    }

    fun setConvTopic(convTopic: String) {
        sharedPreferences.edit().putString(CONV_TOPIC_NAME, convTopic).apply()
    }

    fun getConvTopic(): String {
        return sharedPreferences.getString(CONV_TOPIC_NAME, null) ?: ""
    }

    fun setSelectedAccount(account: SDKConfig) {
        sharedPreferences.edit().putString(SELECTED_ACCOUNT, gson.toJson(account)).apply()
    }

    fun getSelectedAccount(): SDKConfig {
        val json = sharedPreferences.getString(SELECTED_ACCOUNT, null) ?: ""
        return gson.fromJson(json, SDKConfig::class.java) ?: AccountConfig.defaultAccount
    }

    fun setUserAlias(id: String) {
        sharedPreferences.edit().putString(USER_ALIAS, id).apply()
    }

    fun getUserAlias(): String {
        return sharedPreferences.getString(USER_ALIAS, null) ?: ""
    }

    fun setExternalId(id: String) {
        sharedPreferences.edit().putString(EXTERNAL_ID, id).apply()
    }

    fun getExternalId(): String {
        return sharedPreferences.getString(EXTERNAL_ID, null) ?: ""
    }

    fun setRestoreId(id: String) {
        sharedPreferences.edit().putString(RESTORE_ID, id).apply()
    }

    fun getRestoreId(): String {
        return sharedPreferences.getString(RESTORE_ID, null) ?: ""
    }

    fun setTags(tags: String) {
        sharedPreferences.edit().putString(TAGS, tags).apply()
    }

    fun getTags(): String {
        return sharedPreferences.getString(TAGS, null) ?: ""
    }

    fun setFilterType(filterType: String) {
        sharedPreferences.edit().putString(FILTER_TYPE, filterType).apply()
    }

    fun getFilterType(): String {
        return sharedPreferences.getString(FILTER_TYPE, null) ?: ""
    }

    fun setUser(user: User) {
        sharedPreferences.edit().putString(USER, gson.toJson(user)).apply()
    }

    fun getUser(): User {
        val json = sharedPreferences.getString(USER, null) ?: ""
        return gson.fromJson(json, User::class.java) ?: User()
    }

    fun saveUserLocale(locale: String) {
        sharedPreferences.edit().putString(USER_LOCALE, locale).apply()
    }

    fun getUserLocale(): String {
        return sharedPreferences.getString(USER_LOCALE, null) ?: ""
    }

    fun saveWidgetLocale(locale: String) {
        sharedPreferences.edit().putString(WIDGET_LOCALE, locale).apply()
    }

    fun getWidgetLocale(): String {
        return sharedPreferences.getString(WIDGET_LOCALE, null) ?: ""
    }

    fun clearUser() {
        setUserName("")
        saveUserLocale("")
        setUserAlias("")
        setExternalId("")
        setRestoreId("")
        setConvRefId("")
        setConvTopic("")
        setSelectedAccount(getSelectedAccount().copy(jwtAuthToken = ""))
    }
}