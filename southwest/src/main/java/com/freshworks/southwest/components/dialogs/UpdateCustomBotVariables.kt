package com.freshworks.southwest.components.dialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.southwest.R
import com.freshworks.southwest.components.FieldConfig
import com.freshworks.southwest.components.MultiLineFormField
import com.freshworks.southwest.components.buttons.ClearButton
import com.freshworks.southwest.components.buttons.ConfirmButton
import com.freshworks.southwest.components.buttons.DismissButton

@Composable
fun UpdateCustomPropertiesDialog(
    propertiesStringResource: PropertiesStringResource,
    customProperties: String,
    onConfirmed: (String) -> Unit,
    onDismissed: () -> Unit
) {
    val customPropertiesValues = rememberSaveable { mutableStateOf(customProperties) }
    AlertDialog(
        onDismissRequest = {
        },
        title = {
            Text(
                text = stringResource(propertiesStringResource.dialogTitle),
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                CustomPropertiesFormField(
                    propertiesStringResource.propertiesFormDescription,
                    propertiesStringResource.labelId,
                    customPropertiesValues.value
                ) {
                    customPropertiesValues.value = it
                }
            }
        },
        confirmButton = {
            ConfirmButton(R.string.update) {
                onConfirmed.invoke(customPropertiesValues.value)
            }
        },
        dismissButton = {
            DismissButton { onDismissed.invoke() }
        }
    )
}

@Composable
fun CustomPropertiesFormField(
    @StringRes propertiesFormDescription: Int,
    @StringRes labelId: Int,
    customVariables: String,
    onValueChange: (String) -> Unit
) {
    Text(
        text = stringResource(propertiesFormDescription),
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )
    MultiLineFormField(
        labelId = labelId,
        value = customVariables,
        config = FieldConfig(
            isBlank = customVariables.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange.invoke(" ")
                }
            }
        ),
        onValueChange = {
            onValueChange.invoke(it)
        }
    )
}
data class PropertiesStringResource(
    val dialogTitle: Int,
    val propertiesFormDescription: Int,
    val labelId: Int
)
