package com.hocok.fortipass.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.presentation.ui.ActionIcon
import com.hocok.fortipass.presentation.ui.TopBarTitles

@Composable
fun TopBarComponent(
    title: TopBarTitles,
    modifier: Modifier = Modifier,
    back: ActionIcon? = null,
    action: List<ActionIcon> = emptyList()
){
    Box(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.tertiary
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            if (back != null) {
                IconButton(
                    onClick = back.onClick
                ) {
                    Icon(
                        painter = painterResource(back.iconRes),
                        contentDescription = null,
                    )
                }
            }
            Text(
                text = stringResource( title.strId ),
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.weight(1f))
            action.forEach {
                IconButton(
                    onClick = it.onClick
                ) {
                    Icon(
                        painter = painterResource(it.iconRes),
                        contentDescription = null,
                    )
                }
            }
        }
    }

}

