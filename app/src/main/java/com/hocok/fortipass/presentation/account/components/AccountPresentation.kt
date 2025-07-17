package com.hocok.fortipass.presentation.account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.R
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.presentation.ui.ActionButton
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.mainTextColor
import com.hocok.fortipass.presentation.ui.theme.onSecondColor
import com.hocok.fortipass.presentation.ui.theme.secondColor
import com.hocok.fortipass.presentation.ui.theme.selectedItemColor

/**
 * Box for login presentation
 * */
@Composable
fun AccountPresentation(
    account: Account,
    modifier: Modifier = Modifier,
    onFavoriteClick: () -> Unit = {},
){
    Box(
        modifier = modifier.background(secondColor),
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AccountImage(
                link = account.siteLink
            )
            Spacer(Modifier.width(16.dp))
            AccountShortInfo(
                title = account.title,
                login = account.login
            )
            Spacer(Modifier.weight(1f))
            AccountActionIcon(
                actionIcon = ActionButton.ActionIcon(
                    onClick = onFavoriteClick,
                    iconRes = R.drawable.star,
                ),
                isFavorite = account.isFavorite,
                modifier = Modifier.padding(end = 10.dp)
            )
            AccountActionIcon(
                actionIcon = ActionButton.ActionIcon(
                    onClick = {
                        /*TODO("сделать меню")*/
                    },
                    iconRes = R.drawable.like_menu,
                ),
            )
        }
    }
}

/*
* TODO(Replace with Coin)*/
@Composable
private fun AccountImage(
    link: String,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.size(33.dp)
            .clip(RoundedCornerShape(10.dp))
    ){
        Icon(
            painter = painterResource( R.drawable.account ),
            contentDescription = null,
            tint = mainTextColor,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun AccountShortInfo(
    title:String,
    login: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = login,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AccountActionIcon(
    actionIcon: ActionButton.ActionIcon,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false
){
    IconButton(
        onClick = actionIcon.onClick,
        modifier = modifier.size(30.dp)
    ) {
        Icon(
            painter = painterResource(actionIcon.iconRes),
            contentDescription = null,
            tint = if (isFavorite) selectedItemColor else onSecondColor
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun AccountPresentationPreview(){
    FortiPassTheme {
        AccountPresentation(
            account = Account(
            login = "exampleLogin",
            password = "password123456",
            siteLink = "gitHub",
            isFavorite = true,
            title = "GitHub"
        ))
    }
}