package com.freshworks.southwest.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
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
import com.freshworks.southwest.data.DialogConfig
import com.freshworks.southwest.data.IdentifyUserData
import com.freshworks.southwest.ui.theme.SouthWestTheme

@Composable
fun IdentifyUserDialog(
    config: DialogConfig,
    userData: IdentifyUserData,
    onConfirmed: (IdentifyUserData) -> Unit,
    onDismissed: () -> Unit
) {
    val user = rememberSaveable { mutableStateOf(userData) }
    AlertDialog(
        onDismissRequest = {
        },
        title = {
            Text(
                text = stringResource(id = config.title),
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                CopyableField(labelId = R.string.user_alias, value = user.value.userAlias) {
                    onDismissed.invoke()
                }
                FormField(
                    labelId = R.string.external_id,
                    value = user.value.externalId,
                    config = FieldConfig(
                        isBlank = user.value.externalId.isBlank(),
                        trailingIcon = {
                            ClearButton {
                                user.value = user.value.copy(externalId = "")
                            }
                        }
                    ),
                    onValueChange = { user.value = user.value.copy(externalId = it) }
                )

                FormField(
                    labelId = R.string.restore_id,
                    value = user.value.restoreId,
                    config = FieldConfig(
                        isBlank = user.value.restoreId.isBlank(),
                        trailingIcon = {
                            ClearButton {
                                user.value = user.value.copy(restoreId = "")
                            }
                        }
                    ),
                    onValueChange = { user.value = user.value.copy(restoreId = it) }
                )
            }
        },
        confirmButton = {
            ConfirmButton(config.positiveText) {
                onConfirmed(user.value)
            }
        },
        dismissButton = {
            DismissButton { onDismissed.invoke() }
        }
    )
}

@Composable
@Preview
fun ShowIdentifyUserDialog() {
    SouthWestTheme {
        IdentifyUserDialog(
            config = DialogConfig(R.string.identify_user, R.string.identify_show),
            userData = IdentifyUserData("user123", "", ""),
            onConfirmed = {}
        ) {}
    }
}