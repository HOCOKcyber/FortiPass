package com.hocok.fortipass.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hocok.fortipass.presentation.ui.ActionButton
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.selectedItemColor

@Composable
fun FortiPassDialog(
    title : String,
    actionList: List<ActionButton.ActionText>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
){
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(modifier = modifier
            .clip(fullRoundedCorner)
            .background(selectedItemColor)
            .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 20.sp,
                )
                actionList.forEach{
                    Text(
                        text = stringResource(it.textRes),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            it.onClick()
                        }.padding(vertical = 8.dp),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

