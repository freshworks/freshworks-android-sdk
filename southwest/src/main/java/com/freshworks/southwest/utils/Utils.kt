package com.freshworks.southwest.utils

import android.content.Context
import android.widget.Toast
import com.freshworks.southwest.R

val languageMap = mapOf(
    R.string.none to "",
    R.string.arabic to "ar",
    R.string.english to "en",
    R.string.catalan to "ca",
    R.string.chinese to "zh-HANS",
    R.string.chinese_traditional to "zh-HANT",
    R.string.c_zech to "cs",
    R.string.danish to "da",
    R.string.dutch to "nl",
    R.string.german to "de",
    R.string.estonian to "et",
    R.string.finnish to "fi",
    R.string.french to "fr",
    R.string.hungarian to "hu",
    R.string.indonesian to "id",
    R.string.italian to "it",
    R.string.korean to "ko",
    R.string.latvian to "lv",
    R.string.norwegian to "nb",
    R.string.polish to "pl",
    R.string.portuguese to "pt",
    R.string.portuguese_brazil to "pt-BR",
    R.string.romanian to "ro",
    R.string.russian to "ru",
    R.string.slovak to "sk",
    R.string.slovenian to "sl",
    R.string.spanish to "es",
    R.string.spanish_latin_america to "es-LA",
    R.string.swedish to "sv",
    R.string.thai to "th",
    R.string.turkish to "tr",
    R.string.ukrainian to "uk",
    R.string.vietnamese to "vi"
)

fun getLanguageRes(key: Int): String {
    return languageMap[key] ?: "en"
}

fun String.toMap(): Map<String, String> {
    return if (isNotEmpty()) {
        split(",").map { it.split(":") }.associate { (key, value) -> key to value }
    } else {
        emptyMap()
    }
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(
        this,
        text,
        duration
    ).show()
}