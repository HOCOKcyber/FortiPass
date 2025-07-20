package com.hocok.fortipass.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.hocok.fortipass.presentation.ui.theme.onSecondColor

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
){
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        modifier = modifier,
        cursorBrush = Brush.verticalGradient(
            colors = listOf(onSecondColor, onSecondColor)
        ),

    ){
        Box(){
            if (value.isEmpty() && placeholder.isNotEmpty()) Text(text = placeholder,
                style = MaterialTheme.typography.bodyMedium
            )
            it()
        }
    }
}