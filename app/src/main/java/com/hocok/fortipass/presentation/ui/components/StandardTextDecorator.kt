package com.hocok.fortipass.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.ui.ActionButton
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.secondColor

@Composable
fun StandardTextDecorator(
    modifier: Modifier = Modifier,
    title: String = "",
    action: List<ActionButton.ActionIcon> = emptyList(),
    content: @Composable (modifierFocusRequester: Modifier) -> Unit,
){
    val focusRequester = remember { FocusRequester() }

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
                modifier = Modifier.weight(1f).clickable {
                    focusRequester.requestFocus()
                }
            ) {
                if (title.isNotEmpty()){
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodySmall,
                    )
                    Spacer(Modifier.height(5.dp))
                }
                content(
                    Modifier.focusRequester(focusRequester)
                )
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

@Preview(
    showBackground = true
)
@Composable
private fun FortiPassStandardEditTextPreview(){
    FortiPassTheme {
        StandardTextDecorator(
            title = "Название элемента",
            action = listOf(
                ActionButton.ActionIcon(iconRes = R.drawable.visibility, onClick = {}),
                ActionButton.ActionIcon(iconRes = R.drawable.lock, onClick = {  }),
            )
        ) {
                Text(
                    text = "GitHub"
                )
            }
    }
}