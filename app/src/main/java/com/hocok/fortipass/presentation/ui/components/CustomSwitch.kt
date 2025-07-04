package com.hocok.fortipass.presentation.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.onSecondColor
import com.hocok.fortipass.presentation.ui.theme.secondColor

@Composable
fun CustomSwitchButton(
    switchPadding: Dp,
    buttonWidth: Dp,
    buttonHeight: Dp,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    trackColor: Color = secondColor,
    thumbColor: Color = onSecondColor,
) {

    val switchSize by remember {
        mutableStateOf(buttonHeight-switchPadding*2)
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    var padding by remember {
        mutableStateOf(0.dp)
    }

    padding = if (checked) buttonWidth-switchSize-switchPadding*2 else 0.dp

    val animateSize by animateDpAsState(
        targetValue = if (checked) padding else 0.dp,
        tween(
            durationMillis = 700,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ), label = ""
    )

    Box(
        modifier = Modifier
            .width(buttonWidth)
            .height(buttonHeight)
            .clip(CircleShape)
            .background(trackColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {

                onCheckedChange(checked)

            }
    ){
        Row(modifier = Modifier.fillMaxSize().padding(switchPadding)) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(animateSize)
                    .background(Color.Transparent)
            )

            Box(modifier = Modifier
                .size(switchSize)
                .clip(CircleShape)
                .background(thumbColor))

        }
    }

}

@Preview
@Composable
fun SwitchPreview(){
    FortiPassTheme {
        CustomSwitchButton(
            switchPadding = 10.dp,
            buttonHeight = 40.dp,
            buttonWidth = 100.dp,
            onCheckedChange = {},
            checked = true,
        )
    }
}