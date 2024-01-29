package com.freshworks.southwest.components.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.southwest.R
import com.freshworks.southwest.ui.theme.SouthWestTheme

@Composable
fun DrawerHeader(userName: String = stringResource(id = R.string.login), onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.clickable {
            onClick.invoke()
        }
    ) {
        Row(
            modifier = Modifier
                .height(100.dp)
                .width(272.dp)
                .background(color = MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = userName,
                modifier = Modifier
                    .padding(start = 24.dp)
            )
            Text(
                text = userName,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(start = 14.dp)
            )
        }
    }
}

@Preview
@Composable
fun ShowDrawerHeader() {
    SouthWestTheme {
        DrawerHeader()
    }
}
