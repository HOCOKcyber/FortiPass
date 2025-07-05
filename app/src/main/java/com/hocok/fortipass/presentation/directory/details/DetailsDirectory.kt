package com.hocok.fortipass.presentation.directory.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.model.ExampleAccount
import com.hocok.fortipass.domain.model.ExampleDirectory
import com.hocok.fortipass.presentation.account.components.AccountPresentation
import com.hocok.fortipass.presentation.ui.ActionIcon
import com.hocok.fortipass.presentation.ui.components.TopBarComponent
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme

@Composable
fun DetailsDirectoryPage(
    toDetailsAccount: (id: Int)-> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
){
    val viewModel = hiltViewModel<DetailsDirectoryViewModel>()
    val state by viewModel.state.collectAsState()

    DetailsDirectoryPageContent(
        toDetailsAccount = toDetailsAccount,
        directory = state.directory,
        accountList = state.accountsList,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun DetailsDirectoryPageContent(
    accountList: List<Account>,
    directory: Directory,
    toDetailsAccount: (id: Int)-> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
){
    Scaffold(
        topBar = {
            TopBarComponent(
                modifier = Modifier.fillMaxWidth(),
                title = directory.name,
                back = ActionIcon(
                        iconRes = R.drawable.close,
                        onClick = onBack
                )
            )
        },
        modifier = modifier,
    ) {contentPadding ->
        LazyColumn(
            modifier =  Modifier.padding(contentPadding).padding(top = 10.dp)
                .padding(horizontal = 20.dp)
                .clip(fullRoundedCorner)
        ) {
            items(accountList, key = {it.id!!}){
                AccountPresentation(
                    account = it,
                    onFavoriteClick = {/*No action on details*/},
                    modifier = Modifier
                        .padding(top = 1.dp)
                        .clickable { toDetailsAccount(it.id!!) }
                )
            }
        }
    }

}

@Preview
@Composable
fun DetailsDirectoryPagePreview(){
    FortiPassTheme {
        DetailsDirectoryPageContent(
            toDetailsAccount = {},
            onBack = {},
            accountList = ExampleAccount.listOfAccount,
            directory = ExampleDirectory.singleDirectory
        )
    }
}