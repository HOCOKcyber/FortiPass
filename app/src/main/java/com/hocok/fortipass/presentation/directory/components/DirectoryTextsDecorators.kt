package com.hocok.fortipass.presentation.directory.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

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