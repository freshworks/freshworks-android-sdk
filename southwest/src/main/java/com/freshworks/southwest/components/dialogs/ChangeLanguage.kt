package com.freshworks.southwest.components.dialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freshworks.southwest.R
import com.freshworks.southwest.components.buttons.ButtonText
import com.freshworks.southwest.components.buttons.ConfirmButton
import com.freshworks.southwest.components.buttons.DismissButton
import com.freshworks.southwest.ui.theme.SouthWestTheme
import com.freshworks.southwest.utils.getLanguageRes
import com.freshworks.southwest.utils.languageMap

@Composable
fun ChangeLanguageDialog(
    @StringRes title: Int,
    currentLanguageCode: String,
    onConfirmed: (String) -> Unit,
    onDismissed: () -> Unit
) {
    val selectedLanguage = rememberSaveable { mutableStateOf(currentLanguageCode) }
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(
                text = stringResource(id = title)
            )
        },
        text = {
            LanguageList(title, languageMap, selectedLanguage.value, onLanguageChanged = {
                selectedLanguage.value = it
            })
        },
        confirmButton = {
            ConfirmButton(R.string.change) {
                onConfirmed(
                    selectedLanguage.value
                )
            }
        },
        dismissButton = {
            DismissButton { onDismissed.invoke() }
        }
    )
}

@Composable
fun LanguageList(
    @StringRes title: Int,
    languages: Map<Int, String>,
    chosenLanguage: String,
    onLanguageChanged: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.height(180.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        items(languages.keys.size) { index ->
            if (index != 0 || title != R.string.user_language) {
                LanguageItem(
                    languageName = languages.keys.toList()[index],
                    isSelected = getLanguageRes(languages.keys.toList()[index]) == chosenLanguage,
                    onItemSelected = {
                        onLanguageChanged(it)
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    @StringRes languageName: Int,
    isSelected: Boolean,
    onItemSelected: (String) -> Unit
) {
    ButtonText(
        textId = languageName,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onTertiary
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.tertiary
            }
        ),
        modifier = Modifier.fillMaxSize()
    ) {
        onItemSelected(getLanguageRes(languageName))
    }
}

@Preview
@Composable
fun ShowChangeLanguageDialog() {
    SouthWestTheme {
        ChangeLanguageDialog(
            title = R.string.user_language,
            currentLanguageCode = "",
            onConfirmed = {},
            onDismissed = {}
        )
    }
}
