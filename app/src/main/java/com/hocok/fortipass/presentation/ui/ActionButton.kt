package com.hocok.fortipass.presentation.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.hocok.fortipass.presentation.ui.theme.onSecondColor
import com.hocok.fortipass.presentation.ui.theme.secondColor


sealed class ActionButton(
    val onClick: () -> Unit,
){
    class ActionIcon(
        @DrawableRes val iconRes: Int,
        val color: Color = onSecondColor,
        onClick: () -> Unit,
    ): ActionButton(onClick)

    class ActionText(
        @StringRes val textRes: Int,
        val color: Color = secondColor,
        onClick: () -> Unit,
    ): ActionButton(onClick)
}