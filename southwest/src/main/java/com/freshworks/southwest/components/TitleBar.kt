package com.freshworks.southwest.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.SemiBold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.southwest.R
import com.freshworks.southwest.ui.theme.SouthWestTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(
    userName: String = "",
    unreadCount: Int = 0,
    @DrawableRes navigationIcon: Int = R.drawable.ic_menu,
    isTitleNeeded: Boolean = true,
    onClicked: () -> Unit
) {
    TopAppBar(
        title = {
            if (isTitleNeeded) {
                Column {
                    if (userName != "") {
                        Text(
                            text = stringResource(id = R.string.welcome_back),
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontSize = 12.sp,
                        )
                        Text(
                            text = userName,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = SemiBold
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.welcome),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = SemiBold,
                        )
                    }
                }
            }
        },
        navigationIcon = {
            if (unreadCount != 0) {
                BadgedBox(
                    badge = {
                        Badge(
                            modifier = Modifier.size(8.dp)
                        ) {}
                    },
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable {
                            onClicked.invoke()
                        }
                ) {
                    Icon(
                        painter = painterResource(id = navigationIcon),
                        contentDescription = stringResource(id = R.string.menu)
                    )
                }
            } else {
                IconButton(onClick = onClicked) {
                    Icon(
                        painter = painterResource(id = navigationIcon),
                        contentDescription = stringResource(id = R.string.menu)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun ShowTitleBar() {
    SouthWestTheme {
        TitleBar("Durairaj") {}
    }
}
