package com.freshworks.southwest.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.sp
import com.freshworks.sdk.data.SDKConfig
import com.freshworks.southwest.R
import com.freshworks.southwest.components.FieldConfig
import com.freshworks.southwest.components.FormField
import com.freshworks.southwest.components.buttons.ClearButton
import com.freshworks.southwest.ui.theme.SouthWestTheme

/**
 * Created by Robin Rex G on 23/11/23.
 */
@Composable
fun LoadAccountForm(account: SDKConfig, onLoad: (SDKConfig) -> Unit, onDismiss: () -> Unit) {
    val selectedAccount = rememberSaveable { mutableStateOf(account) }
    val isConfirmEnabled = rememberSaveable { mutableStateOf(true) }
    AlertDialog(
        onDismissRequest = {
        },
        title = {
            Text(
                text = stringResource(id = R.string.load_account)
            )
        },
        text = {
            AccountFormFields(selectedAccount.value) {
                selectedAccount.value = it
                isConfirmEnabled.value = !isMandatoryFieldEmpty(it)
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onLoad(selectedAccount.value)
                },
                enabled = isConfirmEnabled.value,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = stringResource(id = R.string.update)
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(
                    text = stringResource(R.string.dismiss)
                )
            }
        }
    )
}

@Composable
fun AccountFormFields(selectedAccount: SDKConfig, onValueChange: (SDKConfig) -> Unit) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        MandatoryFields(
            selectedAccount = selectedAccount,
            onValueChange = { onValueChange.invoke(it) }
        )
        FormField(
            labelId = R.string.widget_id,
            value = selectedAccount.widgetId ?: "",
            config = FieldConfig(
                isRequired = false,
                isBlank = (selectedAccount.widgetId ?: "").isBlank(),
                trailingIcon = {
                    ClearButton {
                        onValueChange(
                            selectedAccount.copy(widgetId = "")
                        )
                    }
                }
            ),
            onValueChange = {
                onValueChange(
                    selectedAccount.copy(widgetId = it.trim())
                )
            }
        )
        AuthTokenField(selectedAccount = selectedAccount, onValueChange = onValueChange)
    }
}

@Composable
fun MandatoryFields(selectedAccount: SDKConfig, onValueChange: (SDKConfig) -> Unit) {
    AppIdAndKey(selectedAccount = selectedAccount, onValueChange = { onValueChange.invoke(it) })
    FormField(
        labelId = R.string.app_domain,
        value = selectedAccount.domain,
        config = FieldConfig(
            isRequired = true,
            isBlank = selectedAccount.domain.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange(
                        selectedAccount.copy(domain = "")
                    )
                }
            }
        ),
        onValueChange = {
            onValueChange(
                selectedAccount.copy(domain = it.trim())
            )
        }
    )
    FormField(
        labelId = R.string.widget_source,
        value = selectedAccount.widgetUrl,
        config = FieldConfig(
            isRequired = true,
            isBlank = selectedAccount.widgetUrl.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange.invoke(
                        selectedAccount.copy(widgetUrl = "")
                    )
                }
            }
        ),
        onValueChange = {
            onValueChange.invoke(
                selectedAccount.copy(widgetUrl = it.trim())
            )
        }
    )
}

@Composable
fun AppIdAndKey(selectedAccount: SDKConfig, onValueChange: (SDKConfig) -> Unit) {
    FormField(
        labelId = R.string.app_id,
        value = selectedAccount.appId,
        config = FieldConfig(
            isRequired = true,
            isBlank = selectedAccount.appId.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange(
                        selectedAccount.copy(appId = "")
                    )
                }
            }
        ),
        onValueChange = {
            onValueChange(
                selectedAccount.copy(appId = it.trim())
            )
        }
    )
    FormField(
        labelId = R.string.app_key,
        value = selectedAccount.appKey,
        config = FieldConfig(
            isRequired = true,
            isBlank = selectedAccount.appKey.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange(
                        selectedAccount.copy(appKey = "")
                    )
                }
            }
        ),
        onValueChange = {
            onValueChange(
                selectedAccount.copy(appKey = it.trim())
            )
        }
    )
}

@Composable
fun AuthTokenField(selectedAccount: SDKConfig, onValueChange: (SDKConfig) -> Unit) {
    Text(
        text = stringResource(id = R.string.auth_token_desc),
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )
    FormField(
        labelId = R.string.auth_token,
        value = selectedAccount.jwtAuthToken,
        config = FieldConfig(
            isRequired = false,
            isBlank = selectedAccount.jwtAuthToken.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange(
                        selectedAccount.copy(jwtAuthToken = "")
                    )
                }
            }
        ),
        onValueChange = {
            onValueChange(
                selectedAccount.copy(jwtAuthToken = it.trim())
            )
        }
    )
}

private fun isMandatoryFieldEmpty(account: SDKConfig): Boolean {
    return (
        account.widgetUrl.isBlank() ||
            account.appId.isBlank() || account.appKey.isBlank() || account.domain.isBlank()
        )
}

@Composable
@Preview
fun ShowLoadAccountForm() {
    SouthWestTheme {
        LoadAccountForm(account = SDKConfig("", "", "", "", widgetId = "test2898"), onLoad = {}) {
        }
    }
}
