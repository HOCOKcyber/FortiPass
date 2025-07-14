package com.hocok.fortipass.presentation.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.onSecondColor
import com.hocok.fortipass.presentation.ui.theme.secondColor

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isShowValue: Boolean,
    onShowValueChange: () -> Unit,
    modifier: Modifier = Modifier,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(fullRoundedCorner)
            .background(color = secondColor)

    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            cursorBrush = SolidColor(onSecondColor),
            modifier = Modifier.weight(1f).padding(start = 10.dp),
            visualTransformation = if (isShowValue) VisualTransformation.None
            else PasswordVisualTransformation()
        )
        IconButton(
            onClick = onShowValueChange,
            modifier = Modifier.padding(end = 10.dp)
        ) {
            Icon(
                painter = painterResource( if (!isShowValue) R.drawable.visibility
                else R.drawable.visibility_off ),
                tint = onSecondColor,
                contentDescription = null,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AuthTextFieldPreview(){
    FortiPassTheme {
        AuthTextField(
            value = "password",
            isShowValue = false,
            onValueChange = {},
            onShowValueChange = {}
        )
    }
}