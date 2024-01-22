package com.freshworks.southwest.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.freshworks.southwest.R
import com.freshworks.southwest.components.CopyableField
import com.freshworks.southwest.components.FieldConfig
import com.freshworks.southwest.components.FormField
import com.freshworks.southwest.components.buttons.ClearButton
import com.freshworks.southwest.components.buttons.ConfirmButton
import com.freshworks.southwest.components.buttons.DismissButton
import com.freshworks.southwest.ui.theme.SouthWestTheme
import java.util.Locale

@Composable
fun UpdateAuthTokenDialog(
    userState: String,
    uuid: String,
    authTokenValue: String,
    onValueChange: (String) -> Unit,
    onDismissed: () -> Unit
) {
    val authToken = rememberSaveable { mutableStateOf(authTokenValue) }
    AlertDialog(
        onDismissRequest = {
        },
        title = { Text(text = stringResource(id = R.string.update_jwt_auth_token)) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(
                    rememberScrollState()
                )
            ) {
                FormField(
                    labelId = R.string.user_state,
                    value = userState.uppercase(Locale.ROOT),
                    config = FieldConfig(isReadOnly = true),
                    onValueChange = {}
                )
                CopyableField(labelId = R.string.uuid, value = uuid) {
                    onDismissed.invoke()
                }
                FormField(
                    labelId = R.string.jwt_token,
                    value = authToken.value,
                    config = FieldConfig(
                        isRequired = false,
                        isBlank = authToken.value.isBlank(),
                        trailingIcon = {
                            ClearButton {
                                authToken.value = ""
                            }
                        }
                    ),
                    onValueChange = {
                        authToken.value = it
                    }
                )
            }
        },
        confirmButton = {
            ConfirmButton(R.string.authenticate) {
                onValueChange.invoke(authToken.value)
            }
        },
        dismissButton = {
            DismissButton {
                onDismissed.invoke()
            }
        }
    )
}

@Preview
@Composable
fun ShowSelectorDialog() {
    SouthWestTheme {
        UpdateAuthTokenDialog(
            userState = "AUTHENTICATED",
            uuid = "fqfs2e6232-121wvdhgw2e70vsywfs267ew-vwydf2yd72te",
            authTokenValue = "",
            onValueChange = {}
        ) {}
    }
}