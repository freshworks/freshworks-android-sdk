package com.freshworks.southwest.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.freshworks.southwest.R
import com.freshworks.southwest.ui.theme.FormFieldTheme
import java.util.Locale

@Composable
fun CopyableField(@StringRes labelId: Int, value: String, onCopy: () -> Unit) {
    val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE)
    val label = stringResource(id = labelId)
    FormField(
        labelId = labelId,
        value = value,
        config = FieldConfig(
            isReadOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.copy),
                    contentDescription = stringResource(id = R.string.uuid),
                    modifier = Modifier
                        .clickable(
                            enabled = value != stringResource(id = R.string.not_found).uppercase(
                                Locale.ROOT
                            )
                        ) {
                            (clipboardManager as? ClipboardManager)?.setPrimaryClip(
                                ClipData.newPlainText(
                                    label,
                                    value
                                )
                            )
                            onCopy.invoke()
                        }
                )
            }
        ),
        onValueChange = {}
    )
}

@Preview
@Composable
fun ShowCopyableField() {
    FormFieldTheme {
        CopyableField(labelId = R.string.user_alias, value = "user_alias_1234") {
        }
    }
}