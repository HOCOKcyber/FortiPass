package com.hocok.fortipass.presentation.account.addedit

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.account.components.AccountInfoWrapper
import com.hocok.fortipass.presentation.directory.components.DirectoryText
import com.hocok.fortipass.presentation.ui.ActionIcon
import com.hocok.fortipass.presentation.ui.TopBarTitles
import com.hocok.fortipass.presentation.ui.bottomRoundedCorner
import com.hocok.fortipass.presentation.ui.components.TopBarComponent
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.onSecondColor
import com.hocok.fortipass.presentation.ui.theme.secondaryTextColor
import com.hocok.fortipass.presentation.ui.theme.selectedItemColor
import com.hocok.fortipass.presentation.ui.topRoundedCorner


@Composable
fun AddEditAccountPage(
    title: TopBarTitles,
    password: String,
    onBack: () -> Unit,
    toGenerator: () -> Unit,
    modifier: Modifier = Modifier,
){
    val viewModel: AddEditAccountViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(password) {
        viewModel.onEvent(AddEditAccountEvent.ChangePassword(password))
    }

    AddEditAccountPageContent(
        title = title,
        isFavorite = state.account.isFavorite,
        accountTitle = state.account.title,
        login = state.account.login,
        password = state.account.password,
        siteLink = state.account.siteLink,
        isPasswordVisible = state.isPasswordVisible,
        changeAccountTitle = {viewModel.onEvent(AddEditAccountEvent.ChangeTitle(it))},
        changeLogin = {viewModel.onEvent(AddEditAccountEvent.ChangeLogin(it)) },
        changePassword = {viewModel.onEvent(AddEditAccountEvent.ChangePassword(it))},
        changeSiteLink = {viewModel.onEvent(AddEditAccountEvent.ChangeSiteLink(it))},
        changePasswordVisible = {viewModel.onEvent(AddEditAccountEvent.ChangePasswordVisible)},
        changeFavorite = {viewModel.onEvent(AddEditAccountEvent.ChangeFavorite)},
        onBack = onBack,
        onSave = {toastCallBack -> viewModel.onEvent(
            AddEditAccountEvent.OnSave(
                toastCallBack = toastCallBack,
                onBack = onBack
            )
        ) },
        toGenerator = toGenerator,
        modifier = modifier,
    )
}

@Composable
private fun AddEditAccountPageContent(
    title: TopBarTitles,
    isFavorite: Boolean,
    accountTitle: String,
    login: String,
    password: String,
    siteLink: String,
    isPasswordVisible: Boolean,
    changePasswordVisible: () -> Unit,
    changeFavorite: () -> Unit,
    changeAccountTitle: (String) -> Unit,
    changeLogin: (String) -> Unit,
    changePassword: (String) -> Unit,
    changeSiteLink: (String) -> Unit,
    onSave: (toastCallBack: (String) -> Unit) -> Unit,
    onBack: () -> Unit,
    toGenerator: () -> Unit,
    modifier: Modifier = Modifier,
){
    val context = LocalContext.current
    val (titleFocus, loginFocus, passwordFocus, linkFocus) = FocusRequester.createRefs()

    Scaffold(
        topBar = {
            TopBarComponent(
                modifier = Modifier.fillMaxWidth(),
                title = title,
                action = listOf(
                    ActionIcon(iconRes = R.drawable.done, onClick = {
                                onSave {Toast.makeText(context, it, Toast.LENGTH_LONG).show() } })
                ),
                back = ActionIcon(iconRes = R.drawable.close, onClick = onBack)
            )
        },

        modifier = modifier.fillMaxSize()
    ) { contentPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 20.dp),
        ) {
            Spacer(Modifier.height(10.dp))

            AccountInfoWrapper(
                title = stringResource(R.string.title_account),
                action = listOf(
                    ActionIcon(iconRes = R.drawable.star,
                        onClick = changeFavorite,
                        color = if (isFavorite) selectedItemColor
                        else onSecondColor)
                ),
                modifier = Modifier.clickable { titleFocus.requestFocus() }
                    .padding(bottom = 1.dp).clip(topRoundedCorner)
            ){
                AddEditTextField(
                    value = accountTitle,
                    onValueChange = changeAccountTitle,
                    modifier = Modifier.focusRequester(titleFocus)
                )
            }

            AccountInfoWrapper(
                title = stringResource(R.string.choose_directory),
                action = listOf(
                    ActionIcon(iconRes = R.drawable.expand, onClick = {})
                ),
                modifier = Modifier.clip(bottomRoundedCorner)
            ){
                /*TODO("Replace with account directory")*/
                DirectoryText(
                    text = "Без папки",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.data_for_authorization),
                style = MaterialTheme.typography.bodyLarge,
                color = secondaryTextColor,
            )

            AccountInfoWrapper(
                title = stringResource(R.string.name_account),
                modifier = Modifier.clickable { loginFocus.requestFocus() }
                    .padding(bottom = 1.dp).clip(topRoundedCorner)
            ) {
                AddEditTextField(
                    value = login,
                    onValueChange = changeLogin,
                    modifier = Modifier.focusRequester(loginFocus)
                )
            }
            AccountInfoWrapper(
                title = stringResource(R.string.password),
                action = listOf(
                    ActionIcon(
                        iconRes =   if (isPasswordVisible) R.drawable.visibility
                        else R.drawable.visibility_off,
                        onClick = changePasswordVisible
                    ),
                    ActionIcon(iconRes = R.drawable.lock, onClick = toGenerator)
                ),
                modifier = Modifier.clickable { passwordFocus.requestFocus() }
                    .clip(bottomRoundedCorner)
            ){
                AddEditTextField(
                    value = password,
                    onValueChange = changePassword,
                    isTextVisible = isPasswordVisible,
                    modifier = Modifier.focusRequester(passwordFocus)
                )
            }

            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.options_for_autocompletion),
                style = MaterialTheme.typography.bodyLarge,
                color = secondaryTextColor,
            )
            AccountInfoWrapper(
                title = stringResource(R.string.site_name),
                modifier = Modifier.clickable { linkFocus.requestFocus() }
                    .clip(fullRoundedCorner)
            ){
                AddEditTextField(
                    value = siteLink,
                    onValueChange = changeSiteLink,
                    modifier = Modifier.focusRequester(linkFocus)
                )
            }
        }
    }
}

@Composable
private fun AddEditTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isTextVisible: Boolean = true,
){
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        visualTransformation = if (isTextVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isTextVisible) KeyboardType.Text else KeyboardType.Password
        ),
        modifier = modifier,
        cursorBrush = Brush.verticalGradient(
            colors = listOf(onSecondColor, onSecondColor)
        )
    )
}

@Preview
@Composable
private fun AddEditPreview(){
    FortiPassTheme {
        AddEditAccountPageContent(
            title = TopBarTitles.EDIT,
            isFavorite = false,
            isPasswordVisible = true,
            accountTitle = "GitHub",
            login = "exampleLogin",
            password = "qwerty1234",
            siteLink = "gitHub.com",
            changeAccountTitle = {},
            changeLogin = {},
            changePassword = {},
            changeSiteLink = {},
            changePasswordVisible = {},
            onSave = {_ ->},
            onBack = {},
            changeFavorite = {},
            toGenerator = {}
        )
    }
}