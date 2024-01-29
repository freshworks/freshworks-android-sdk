package com.freshworks.southwest.components.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.freshworks.southwest.ui.theme.SouthWestTheme

const val DIVIDER_ALPHA_07F = 0.07f

@Composable
fun DrawerElement(
    title: String,
    subTitle: String,
    modifier: Modifier = Modifier
        .width(272.dp)
        .height(100.dp),
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clickable {
                onClick.invoke()
            }
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600,
            fontFamily = FontFamily.SansSerif,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 20.dp)
        )
        Text(
            text = subTitle,
            fontSize = 12.sp,
            fontFamily = FontFamily.SansSerif,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 28.dp)
        )
    }
}

@Preview
@Composable
fun ShowDrawerElement() {
    SouthWestTheme {
        DrawerElement(title = "My orders", subTitle = "you have 2 orders")
    }
}

@Composable
fun AddDivider(color: Color = MaterialTheme.colorScheme.primary) {
    Divider(
        thickness = 1.dp,
        color = color,
        modifier = Modifier
            .padding(start = 16.dp, end = 20.dp)
            .alpha(DIVIDER_ALPHA_07F)
    )
}
