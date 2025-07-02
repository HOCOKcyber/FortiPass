package com.hocok.fortipass.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.presentation.ui.ActionIcon
import com.hocok.fortipass.presentation.ui.theme.actionColor

@Composable
fun DecoratorFloatingButton(
    actionIcon: ActionIcon,
    modifier: Modifier = Modifier
){
    IconButton(
        modifier = modifier
            .clip(CircleShape)
            .background(actionColor)
            .size(55.dp),
        onClick = actionIcon.onClick
    ) {
        Icon(
            painter = painterResource( actionIcon.iconRes ),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}