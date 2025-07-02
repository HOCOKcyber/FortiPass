package com.hocok.fortipass.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

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