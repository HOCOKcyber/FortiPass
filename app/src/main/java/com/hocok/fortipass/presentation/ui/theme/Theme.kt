package com.hocok.fortipass.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val colorScheme = lightColorScheme(
    background = backgroundColor,
    onBackground = onBackgroundColor,
    secondary = secondColor,
    onSecondary = onSecondColor,
    tertiary = thirdColor,
    onTertiary = onThirdColor,
    primary = primaryColor,
    onPrimary = onPrimaryColor,
    primaryContainer = secondColor,
)

@Composable
fun FortiPassTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}