package com.freshworks.southwest.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val appColorScheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = Blue,
    surface = Color.White,
    onSurface = Color.Black,
    secondary = DarkBlue,
    onSecondary = Color.LightGray,
    tertiary = Gray,
    onTertiary = Color.Transparent
)

private val drawerColorScheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = Red,
    secondary = Gray
)

private val formFieldTheme = lightColorScheme(
    primary = NavyBlue,
    onPrimary = Red,
    secondary = Gray,
    onSecondary = Color.Blue
)

@Composable
fun SouthWestTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = appColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun NavigationDrawerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = drawerColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun FormFieldTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = formFieldTheme,
        typography = Typography,
        content = content
    )
}
