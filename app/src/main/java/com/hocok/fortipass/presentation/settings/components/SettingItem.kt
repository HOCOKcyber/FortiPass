package com.hocok.fortipass.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.secondColor

@Composable
fun SettingItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 1.dp)
            .background(secondColor)
            .clickable { onClick() }
    ){
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun SettingItemPreview(){
    FortiPassTheme {
        SettingItem(
            title = stringResource(R.string.export_data),
            onClick = {}
        )
    }
}