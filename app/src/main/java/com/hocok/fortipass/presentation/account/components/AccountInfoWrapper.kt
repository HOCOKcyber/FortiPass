package com.hocok.fortipass.presentation.account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.presentation.ui.ActionIcon
import com.hocok.fortipass.presentation.ui.theme.secondColor

@Composable
fun AccountInfoWrapper(
    title: String,
    modifier: Modifier = Modifier,
    action: List<ActionIcon> = emptyList(),
    content: @Composable () -> Unit,
){
    Box(
        modifier = modifier.background(
            color = secondColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                androidx.compose.material3.Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(5.dp))
                content()
            }
            Spacer(Modifier.width(10.dp))

            action.forEachIndexed { index, buttonData ->
                IconButton(
                    onClick = buttonData.onClick,
                    modifier = Modifier
                        .padding(end = if (index != action.lastIndex) 10.dp else 0.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(buttonData.iconRes),
                        contentDescription = null,
                        tint = buttonData.color
                    )
                }

            }
        }
    }
}
