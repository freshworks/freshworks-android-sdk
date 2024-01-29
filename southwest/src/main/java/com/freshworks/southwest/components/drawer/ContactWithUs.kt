package com.freshworks.southwest.components.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.southwest.R
import com.freshworks.southwest.ui.theme.SouthWestTheme

const val NINE_PLUS = "9+"
const val MAX_UNREAD_COUNT = 9

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactWithUs(unreadCount: Int, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .width(272.dp)
            .clickable {
                onClick.invoke()
            }
    ) {
        Column(
            modifier = Modifier
                .width(212.dp)
                .height(100.dp)
        ) {
            Text(
                text = stringResource(id = R.string.contact_us),
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 20.dp)
            )
            Text(
                text = stringResource(id = R.string.talk_to_an_agent),
                fontSize = 12.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 28.dp)
            )
        }
        if (unreadCount != 0) {
            val count = if (unreadCount < MAX_UNREAD_COUNT) unreadCount.toString() else NINE_PLUS
            Badge(
                Modifier
                    .semantics { contentDescription = count }
                    .align(Alignment.CenterVertically)
                    .padding(end = 38.dp)
            ) {
                Text(text = count)
            }
        }
    }
}

@Preview
@Composable
fun ShowContactWithUs() {
    SouthWestTheme {
        ContactWithUs(unreadCount = 10)
    }
}
