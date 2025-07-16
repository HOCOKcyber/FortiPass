package com.hocok.fortipass.presentation.authentication.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.secondColor

@Composable
fun AuthButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = fullRoundedCorner
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = secondColor,
            fontWeight = FontWeight.Bold,
        )
    }
}