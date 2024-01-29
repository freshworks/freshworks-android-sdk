package com.freshworks.southwest.components.dialogs

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.sdk.data.WebWidgetConfig
import com.freshworks.southwest.R
import com.freshworks.southwest.components.FieldConfig
import com.freshworks.southwest.components.FormField
import com.freshworks.southwest.components.buttons.ClearButton
import com.freshworks.southwest.components.buttons.ConfirmButton
import com.freshworks.southwest.components.buttons.DismissButton
import com.freshworks.southwest.data.TagsConfig
import com.freshworks.southwest.ui.theme.SouthWestTheme

@Composable
fun TagsDialog(
    tagsValue: String,
    filterTypeValue: String,
    onConfirmed: (TagsConfig) -> Unit,
    onDismissed: () -> Unit
) {
    val tags = rememberSaveable { mutableStateOf(tagsValue) }
    val filterType = rememberSaveable { mutableStateOf(filterTypeValue) }
    AlertDialog(onDismissRequest = {}, title = {
        Text(
            text = stringResource(id = R.string.conversation_faq_tags),
            color = MaterialTheme.colorScheme.primary
        )
    }, text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TagsFormField(tags.value) { tags.value = it }
            Text(
                text = stringResource(id = R.string.select_filter_type),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp)
            )
            SetFilterType(filterType.value) {
                filterType.value = it
            }
        }
    }, confirmButton = {
        ConfirmButton {
            onConfirmed.invoke(TagsConfig(tags.value, filterType.value))
        }
    }, dismissButton = {
        DismissButton { onDismissed.invoke() }
    })
}

@Composable
fun TagsFormField(tagsValue: String, onValueChange: (String) -> Unit) {
    Text(
        text = stringResource(id = R.string.enter_tags_desc),
        fontSize = 12.sp,
        modifier = Modifier.padding(horizontal = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )
    FormField(
        labelId = R.string.tags,
        value = tagsValue,
        config = FieldConfig(
            isBlank = tagsValue.isBlank(),
            trailingIcon = {
                ClearButton {
                    onValueChange.invoke("")
                }
            }
        ),
        onValueChange = { onValueChange.invoke(it) }
    )
}

@Composable
fun SetFilterType(filterType: String, onValueChange: (String) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxSize()
            .horizontalScroll(
                rememberScrollState()
            )
    ) {
        TextChip(
            textId = R.string.conversation,
            isSelected = filterType == WebWidgetConfig.FilterType.CONVERSATION
        ) {
            onValueChange.invoke(WebWidgetConfig.FilterType.CONVERSATION)
        }
        TextChip(
            textId = R.string.category,
            isSelected = filterType == WebWidgetConfig.FilterType.CATEGORY
        ) {
            onValueChange.invoke(WebWidgetConfig.FilterType.CATEGORY)
        }
        TextChip(
            textId = R.string.article,
            isSelected = filterType == WebWidgetConfig.FilterType.ARTICLE
        ) {
            onValueChange.invoke(WebWidgetConfig.FilterType.ARTICLE)
        }
        TextChip(
            textId = R.string.none,
            isSelected = filterType == WebWidgetConfig.FilterType.NONE
        ) {
            onValueChange.invoke(WebWidgetConfig.FilterType.NONE)
        }
    }
}

@Composable
fun TextChip(textId: Int, isSelected: Boolean, onValueChange: () -> Unit) {
    Button(
        onClick = {
            onValueChange.invoke()
        },
        shape = ShapeDefaults.Medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSecondary
            },
            contentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = stringResource(id = textId),
            maxLines = 1,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Preview
@Composable
fun ShowTagsDialog() {
    SouthWestTheme {
        TagsDialog("mobile", WebWidgetConfig.FilterType.CATEGORY, onConfirmed = {}) {}
    }
}