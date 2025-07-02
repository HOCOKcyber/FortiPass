package com.hocok.fortipass.presentation.directory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.onSecondColor
import com.hocok.fortipass.presentation.ui.theme.secondColor

@Composable
fun DirectoryContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
){
    Box(
        modifier = modifier.fillMaxWidth()
            .background(secondColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp,
                vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.folder),
                contentDescription = null,
                tint = onSecondColor,
            )
            Spacer(Modifier.width(10.dp))
            content()
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun DirectoryPreviewText(){
    FortiPassTheme {
        DirectoryContainer{
            DirectoryText(
                text = "Без папки"
            )
        }
    }
}

@Preview
@Composable
private fun DirectoryPreviewTextField(){
    FortiPassTheme {
        DirectoryContainer{
            DirectoryTextField(
                value = "Сменить папки",
                onValueChange = {}
            )
        }
    }
}