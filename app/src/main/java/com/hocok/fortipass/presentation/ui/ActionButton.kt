package com.hocok.fortipass.presentation.ui

import androidx.annotation.DrawableRes
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.hocok.fortipass.presentation.ui.theme.onSecondColor

data class ActionIcon(
    @DrawableRes val iconRes: Int,
    val onClick: () -> Unit,
    val color: Color = onSecondColor
)