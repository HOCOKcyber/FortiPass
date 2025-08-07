package com.hocok.fortipass.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.ExampleAccount
import com.hocok.fortipass.presentation.account.components.AccountPresentation
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.onPrimaryColor

@Composable
fun SearchPage(
    onBack: () -> Unit,
    toDetailsAccount: (id: Int) -> Unit,
    modifier: Modifier = Modifier
){
    val viewModel = hiltViewModel<SearchViewModel>()
    val state by viewModel.state.collectAsState()

    SearchContent(
        request = state.request,
        responseList = state.listAccount,
        directoryName = state.directoryName,
        changeRequest = {viewModel.onEvent(SearchEvent.ChangeRequest(it))},
        toDetailsAccount = toDetailsAccount,
        onBack = onBack,
        changeSpecificAccountFavorite = {id, isFavorite ->
            viewModel.onEvent(SearchEvent.ChangeFavoriteById(id, isFavorite))},
        modifier = modifier
    )
}

@Composable
private fun SearchContent(
    request: String,
    directoryName: String?,
    responseList: List<Account>,
    changeSpecificAccountFavorite: (id: Int, isFavorite: Boolean) -> Unit,
    toDetailsAccount: (id: Int) -> Unit,
    changeRequest: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ){
            TopSearchBar(
                request = request,
                directoryName = directoryName,
                changeRequest = changeRequest,
                onBack = onBack,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .clip(fullRoundedCorner)
            ) {
                items(responseList, key = {it.id!!}){
                    AccountPresentation(
                        account = it,
                        onFavoriteClick = {changeSpecificAccountFavorite(it.id!!, it.isFavorite)},
                        modifier = Modifier
                            .padding(top = 1.dp)
                            .clickable { toDetailsAccount(it.id!!) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopSearchBar(
    request: String,
    directoryName: String?,
    changeRequest: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        IconButton(
            onClick = onBack
        ) {
            Icon(
                painter = painterResource(R.drawable.close),
                contentDescription = null,
            )
        }
        OutlinedTextField(
            value = request,
            onValueChange = changeRequest,
            shape = fullRoundedCorner,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                )
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search) +
                            if (directoryName != null) " ${stringResource(R.string.in_symbol)} $directoryName"
                            else "",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = onPrimaryColor
                    )
                )
            },
            textStyle = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = onPrimaryColor
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun SearchPagePreview(){
    FortiPassTheme {
        SearchContent(
            request = "",
            responseList = ExampleAccount.listOfAccount,
            directoryName = null,
            changeRequest = {},
            changeSpecificAccountFavorite = {_,_ -> },
            onBack = {},
            toDetailsAccount = {},
        )
    }
}
