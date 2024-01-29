package com.freshworks.southwest.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.freshworks.southwest.R
import com.freshworks.southwest.ui.theme.NavigationDrawerTheme

@Composable
fun LogOut(onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .padding(top = 16.dp, start = 16.dp)
            .clickable {
                onClick.invoke()
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_exit_icon),
            contentDescription = stringResource(
                id = R.string.logout
            )
        )
        Text(
            text = stringResource(id = R.string.logout),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
fun ShowLogout() {
    NavigationDrawerTheme {
        LogOut()
    }
}
