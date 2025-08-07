package com.hocok.fortipass.presentation.authentication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.ui.theme.backgroundColor
import com.hocok.fortipass.presentation.ui.theme.secondColor

@Composable
fun AuthContent(
    title: String,
    description: String,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
){
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {contentPadding ->
        Column(
            modifier = modifier
                .background(backgroundColor)
                .padding(contentPadding)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 40.sp,
                color = secondColor,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = secondColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(20.dp))
            content()
            Spacer(Modifier.weight(1f))
            AuthButton(
                text = stringResource(R.string.continue_text),
                onClick = onContinue,
                modifier = Modifier
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}