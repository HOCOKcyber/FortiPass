package com.hocok.fortipass.presentation.homepage


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.model.ExampleAccount
import com.hocok.fortipass.domain.model.ExampleDirectory
import com.hocok.fortipass.presentation.account.components.AccountPresentation
import com.hocok.fortipass.presentation.directory.components.DirectoryContainer
import com.hocok.fortipass.presentation.directory.components.DirectoryText
import com.hocok.fortipass.presentation.ui.ActionIcon
import com.hocok.fortipass.presentation.ui.TopBarTitles
import com.hocok.fortipass.presentation.ui.components.CustomSwitchButton
import com.hocok.fortipass.presentation.ui.components.DecoratorFloatingButton
import com.hocok.fortipass.presentation.ui.components.TopBarComponent
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.selectedItemColor

@Composable
fun HomePage(
    toAddAccount: () -> Unit,
    toDetailsAccount: (id: Int) -> Unit,
    toAddDirectory: () -> Unit,
    toDetailsDirectory: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
){
    val viewModel = hiltViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()

    HomePageContent(
        isAccountsDisplay = state.isAccountsDisplay,
        isFavoriteDisplay = state.isFavoriteDisplay,
        isDialogDisplay =  state.isDialogDisplay,
        accountsList = state.accountsList,
        directoryList = state.directoryList,
        changeListDisplay = {viewModel.onEvent(HomeEvent.ChangeAccountsDisplay)},
        changeDialogDisplay = {viewModel.onEvent(HomeEvent.ChangeDialogDisplay)},
        changeFavoriteDisplay = {viewModel.onEvent(HomeEvent.ChangeFavoriteDisplay)},
        changeSpecificAccountFavorite = {id, isFavorite -> viewModel.onEvent(HomeEvent.ChangeFavoriteById(id, isFavorite))},
        toDetailsDirectory = toDetailsDirectory,
        toAddAccount = toAddAccount,
        toAddDirectory = toAddDirectory,
        toDetailsAccount = toDetailsAccount,
        modifier = modifier,
    )
}

@Composable
private fun HomePageContent(
    isAccountsDisplay: Boolean,
    isFavoriteDisplay: Boolean,
    isDialogDisplay: Boolean,
    accountsList: List<Account>,
    directoryList: List<Directory>,
    changeListDisplay: () -> Unit,
    changeDialogDisplay: () -> Unit,
    changeFavoriteDisplay: (Boolean) -> Unit,
    changeSpecificAccountFavorite: (id: Int, isFavorite: Boolean) -> Unit,
    toAddAccount: () -> Unit,
    toDetailsAccount: (id: Int) -> Unit,
    toDetailsDirectory: (id: Int) -> Unit,
    toAddDirectory: () -> Unit,
    modifier: Modifier = Modifier,
){
    Scaffold(
        topBar = {
            TopBarComponent(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource( TopBarTitles.STORAGE.strId),
                action = listOf(
                    ActionIcon(
                        iconRes = R.drawable.search,
                        onClick = {/*TODO("realise search")*/}
                    ),
                    ActionIcon(
                        iconRes =   if (isAccountsDisplay) R.drawable.folder
                        else R.drawable.account,
                        onClick = changeListDisplay
                    )
                )
            )
        },
        floatingActionButton = {
            DecoratorFloatingButton(
                actionIcon = ActionIcon(
                    iconRes = R.drawable.add,
                    onClick = changeDialogDisplay
                )
            )
        },
        modifier = modifier
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 20.dp),
        ) {
            if (isAccountsDisplay){
                ShowFavorite(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    isChecked = isFavoriteDisplay,
                    onSwitch = changeFavoriteDisplay
                )
            } else {
                Spacer(Modifier.height(10.dp))
            }
            LazyColumn(
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            ) {
                if (isAccountsDisplay){
                    items(accountsList, key = {it.id!!}) {
                        AccountPresentation(
                            account = it,
                            onFavoriteClick = {changeSpecificAccountFavorite(it.id!!, it.isFavorite)},
                            modifier = Modifier
                                .padding(top = 1.dp)
                                .clickable { toDetailsAccount(it.id!!) }
                        )
                    }
                } else {
                    items(directoryList, key = {it.id!!}){
                        DirectoryContainer(
                            modifier = Modifier
                                .padding(top = 1.dp)
                                .clickable { toDetailsDirectory(it.id!!) }
                        ) {
                            DirectoryText(
                                text = it.name
                            )
                        }
                    }
                }
            }
            if (isDialogDisplay){
                DialogSelectTypeToAdd(
                    onDismissRequest = changeDialogDisplay,
                    toAddLogin = toAddAccount,
                    toAddDirectory = toAddDirectory,
                )
            }
        }
    }
}

@Composable
private fun ShowFavorite(
    modifier: Modifier = Modifier,
    onSwitch: (Boolean) -> Unit = {},
    isChecked: Boolean = false,
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.favorite),
            style = MaterialTheme.typography.labelMedium
        )
        CustomSwitchButton(
            checked = isChecked,
            onCheckedChange = onSwitch,
            switchPadding = 6.dp,
            buttonHeight = 25.dp,
            buttonWidth = 50.dp
        )
    }
}

@Composable
private fun DialogSelectTypeToAdd(
    onDismissRequest: () -> Unit,
    toAddLogin: () -> Unit,
    toAddDirectory: () -> Unit,
    modifier: Modifier = Modifier
){
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(10.dp))
                .background(selectedItemColor)
                .fillMaxWidth()
                .padding(10.dp)
        ){
           Column(
               modifier = Modifier.padding(10.dp)
           ){
               Text(
                   text = stringResource( R.string.type),
                   style = MaterialTheme.typography.bodyLarge,
                   fontSize = 20.sp,
               )
               Spacer(Modifier.height(15.dp))
               TextButton(
                   onClick = {
                       toAddLogin()
                       onDismissRequest()
                             },
                   contentPadding = PaddingValues(0.dp)
               ){
                   Text(
                       text = stringResource( R.string.login),
                       style = MaterialTheme.typography.bodyMedium,
                       fontSize = 18.sp,
                   )
               }
               TextButton(
                   onClick = {
                       toAddDirectory()
                       onDismissRequest()
                   },
                   contentPadding = PaddingValues(0.dp)
               ) {
                   Text(
                       text = stringResource( R.string.directory),
                       style = MaterialTheme.typography.bodyMedium,
                       fontSize = 18.sp,
                   )
               }
           }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogSelectTypeToAddPreview(){
    FortiPassTheme {
        DialogSelectTypeToAdd(
            onDismissRequest = {},
            toAddDirectory = {},
            toAddLogin = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun HomePagePreview(){
    FortiPassTheme {
        HomePageContent(
            isFavoriteDisplay = true,
            isDialogDisplay = false,
            isAccountsDisplay = true,
            accountsList = ExampleAccount.listOfAccount,
            directoryList = ExampleDirectory.listOfDirectory,
            changeSpecificAccountFavorite = {_, _ ->},
            changeListDisplay = {},
            toAddDirectory = {},
            toDetailsAccount = {},
            toAddAccount = {},
            changeFavoriteDisplay = {},
            changeDialogDisplay = {},
            toDetailsDirectory = {}
        )
    }
}
