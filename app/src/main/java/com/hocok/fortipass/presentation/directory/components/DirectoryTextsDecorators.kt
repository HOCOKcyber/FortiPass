package com.hocok.fortipass.presentation.directory.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.ui.theme.onSecondColor

@Composable
fun DirectoryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
){
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = modifier,
        cursorBrush = Brush.verticalGradient(
            colors = listOf(onSecondColor, onSecondColor)
        ),
    ){
        Box(){
            if (value.isEmpty()) Text(text = stringResource(R.string.enter_directory_name))
            it()
        }
    }
}

@Composable
fun DirectoryText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
){
    Text(
        text = text,
        style = style,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
    )
}