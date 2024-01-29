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
import com.freshworks.southwest.R
import com.freshworks.southwest.components.FieldConfig
import com.freshworks.southwest.components.FormField
import com.freshworks.southwest.components.buttons.ClearButton
import com.freshworks.southwest.data.User
import com.freshworks.southwest.ui.theme.SouthWestTheme

@Composable
fun UpdateUserDialog(
    userData: User,
    onConfirmed: (User) -> Unit,
    onDismissed: () -> Unit
) {
    val user = rememberSaveable { mutableStateOf(userData) }
    AlertDialog(
        onDismissRequest = {
        },
        title = {
            Text(
                text = stringResource(id = R.string.update_user),
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                UserFormField(user = user.value, onValueChange = { user.value = it })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmed(
                        user.value
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(text = stringResource(id = R.string.update))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissed.invoke()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        }
    )
}

@Composable
fun UserFormField(user: User, onValueChange: (User) -> Unit) {
    FormField(
        labelId = R.string.first_name,
        value = user.firstName,
        config = FieldConfig(
            isBlank = user.firstName.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange.invoke(user.copy(firstName = ""))
                }
            }
        ),
        onValueChange = {
            onValueChange.invoke(user.copy(firstName = it))
        }
    )
    FormField(
        labelId = R.string.last_name,
        value = user.lastName,
        config = FieldConfig(
            isBlank = user.lastName.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange.invoke(user.copy(lastName = ""))
                }
            }
        ),
        onValueChange = {
            onValueChange.invoke(user.copy(lastName = it))
        }
    )
    ContactDetails(user = user, onValueChange = { onValueChange.invoke(it) })
    CustomProperties(user = user, onValueChange = { onValueChange.invoke(it) })
}

@Composable
fun ContactDetails(user: User, onValueChange: (User) -> Unit) {
    FormField(
        labelId = R.string.email,
        value = user.email,
        config = FieldConfig(
            isBlank = user.email.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange.invoke(user.copy(email = ""))
                }
            }
        ),
        onValueChange = {
            onValueChange.invoke(user.copy(email = it))
        }
    )
    FormField(
        labelId = R.string.phone_country,
        value = user.phoneCountry,
        config = FieldConfig(
            isBlank = user.phoneCountry.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange.invoke(user.copy(phoneCountry = ""))
                }
            }
        ),
        onValueChange = {
            onValueChange.invoke(user.copy(phoneCountry = it))
        }
    )
    FormField(
        labelId = R.string.phone_number,
        value = user.phoneNumber,
        config = FieldConfig(
            isBlank = user.phoneNumber.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange.invoke(user.copy(phoneNumber = ""))
                }
            }
        ),
        onValueChange = {
            onValueChange.invoke(user.copy(phoneNumber = it))
        }
    )
}

@Composable
fun CustomProperties(user: User, onValueChange: (User) -> Unit) {
    Text(
        text = stringResource(id = R.string.properties_description),
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
    FormField(
        labelId = R.string.custom_properties,
        value = user.properties,
        config = FieldConfig(
            isBlank = user.properties.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange.invoke(user.copy(properties = ""))
                }
            }
        ),
        onValueChange = {
            onValueChange.invoke(user.copy(properties = it))
        }
    )
}

@Preview
@Composable
fun ShowUserDialog() {
    SouthWestTheme {
        UpdateUserDialog(userData = User(), onConfirmed = {}) {}
    }
}
