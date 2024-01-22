package com.freshworks.southwest.components.dialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freshworks.southwest.R
import com.freshworks.southwest.ui.theme.SouthWestTheme

@Composable
fun ProgressBarDialog(@StringRes title: Int) {
    AlertDialog(
        onDismissRequest = { },
        title = {
            Text(text = stringResource(id = title))
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(30.dp)
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
        }, dismissButton = {
        }
    )
}

@Preview
@Composable
fun ShowProgressBarDialog() {
    SouthWestTheme {
        ProgressBarDialog(R.string.resetting_user)
    }
}