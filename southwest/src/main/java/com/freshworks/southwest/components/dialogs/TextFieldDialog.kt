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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.southwest.R
import com.freshworks.southwest.components.FieldConfig
import com.freshworks.southwest.components.FormField
import com.freshworks.southwest.components.buttons.ClearButton
import com.freshworks.southwest.components.buttons.ConfirmButton
import com.freshworks.southwest.components.buttons.DismissButton
import com.freshworks.southwest.data.DialogConfig
import com.freshworks.southwest.data.TextFieldDialog
import com.freshworks.southwest.ui.theme.SouthWestTheme

@Composable
fun TextFieldDialog(
    config: DialogConfig,
    textField1: Pair<Int, String>,
    textField2: Pair<Int, String>,
    onConfirmed: (TextFieldDialog) -> Unit,
    onDismissed: () -> Unit
) {
    val field1 = rememberSaveable { mutableStateOf(textField1.second) }
    val field2 = rememberSaveable { mutableStateOf(textField2.second) }
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
                FormField(
                    labelId = textField1.first,
                    value = field1.value,
                    config = FieldConfig(
                        isBlank = field1.value.isBlank(),
                        trailingIcon = {
                            ClearButton { field1.value = "" }
                        }
                    ),
                    onValueChange = { field1.value = it }
                )
                if (config.showDescription) {
                    ShowDescription()
                }
                FormField(
                    labelId = textField2.first,
                    value = field2.value,
                    config = FieldConfig(
                        isBlank = field2.value.isBlank(),
                        trailingIcon = {
                            ClearButton {
                                field2.value = ""
                            }
                        }
                    ),
                    onValueChange = { field2.value = it }
                )
            }
        },
        confirmButton = {
            ConfirmButton(config.positiveText) {
                onConfirmed(
                    TextFieldDialog(
                        field1.value,
                        field2.value
                    )
                )
            }
        },
        dismissButton = {
            DismissButton { onDismissed.invoke() }
        }
    )
}

@Composable
fun ShowDescription(@StringRes description: Int = R.string.event_data_description) {
    Text(
        text = stringResource(id = description),
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}

@Composable
@Preview
fun ShowTextFieldDialog() {
    SouthWestTheme {
        TextFieldDialog(
            config = DialogConfig(R.string.open_Conversation, R.string.open),
            textField1 = Pair(R.string.conv_ref_id, ""),
            textField2 = Pair(R.string.topic_name, ""),
            onConfirmed = {}
        ) {}
    }
}