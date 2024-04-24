package com.freshworks.southwest.components.dialogs

import androidx.annotation.StringRes
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
import com.freshworks.southwest.R
import com.freshworks.southwest.components.FieldConfig
import com.freshworks.southwest.components.MultiLineFormField
import com.freshworks.southwest.components.buttons.ClearButton
import com.freshworks.southwest.components.buttons.ConfirmButton
import com.freshworks.southwest.components.buttons.DismissButton

@Composable
fun LocalizationSupportPropertiesDialog(
    dialogTitle: Int,
    headerProperties: String,
    contentProperties: String,
    onConfirmed: (String, String) -> Unit,
    onDismissed: () -> Unit
) {
    val headerPropertiesValues = rememberSaveable { mutableStateOf(headerProperties) }
    val contentPropertiesValues = rememberSaveable { mutableStateOf(contentProperties) }
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(
                text = stringResource(dialogTitle),
                color = MaterialTheme.colorScheme.primary
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                LocalizationPropertiesFormField(
                    labelId = R.string.config_header_property_label_id,
                    properties = headerPropertiesValues.value,
                    onPropertyValueChanged = { headerPropertiesValues.value = it },
                    placeholderText = "{\n" +
                        "    \"appName\": \"Freshworks sample app\",\n" +
                        "    \"appLogo\": \"https://d1qb2nb5cznatu.cloudfront.net/startups/i/2473" +
                        "-2c38490d8e4c91660d86ff54ba5391ea-medium_jpg.jpg\"\n" +
                        "}"
                )

                LocalizationPropertiesFormField(
                    labelId = R.string.config_content_property_label_id,
                    properties = contentPropertiesValues.value,
                    onPropertyValueChanged = { contentPropertiesValues.value = it },
                    placeholderText = "{\n" +
                        "    \"welcomeMessage\": \"Hello there\",\n" +
                        "    \"headers\": {\n" +
                        "    \"faq\": \"FAQS\",\n" +
                        "    \"chat\": \"Chat with us\",\n" +
                        "    },\n" +
                        "    \"placeholders\": {\n" +
                        "        \"search_field\": \"Search here\"\n" +
                        "    }\n" +
                        "}"
                )
            }
        },
        confirmButton = {
            ConfirmButton(R.string.update) {
                onConfirmed.invoke(headerPropertiesValues.value, contentPropertiesValues.value)
            }
        },
        dismissButton = {
            DismissButton { onDismissed.invoke() }
        }
    )
}

@Composable
fun LocalizationPropertiesFormField(
    @StringRes labelId: Int,
    properties: String,
    onPropertyValueChanged: (String) -> Unit,
    placeholderText: String
) {
    MultiLineFormField(
        labelId = labelId,
        value = properties,
        config = FieldConfig(
            isBlank = properties.isBlank(),
            trailingIcon = {
                ClearButton {
                    onPropertyValueChanged.invoke("")
                }
            }
        ),
        onValueChange = {
            onPropertyValueChanged.invoke(it)
        },
        placeholder = placeholderText
    )
}
