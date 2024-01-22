package com.freshworks.southwest.components.buttons

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.freshworks.southwest.R
import com.freshworks.southwest.ui.theme.SouthWestTheme

@Composable
fun DismissButton(onDismissed: () -> Unit) {
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

@Preview
@Composable
fun ShowDismissButton() {
    SouthWestTheme {
        DismissButton {}
    }
}