package com.hocok.fortipass.presentation.account.addedit

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.model.ExampleAccount
import com.hocok.fortipass.domain.model.ExampleDirectory
import com.hocok.fortipass.presentation.account.components.AccountInfoWrapper
import com.hocok.fortipass.presentation.directory.components.DirectoryContainer
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
import kotlinx.coroutines.launch


@Composable
fun AddEditAccountPage(
    title: String,
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
        account = state.account,
        directory = state.currentDirectory,
        isPasswordVisible = state.isPasswordVisible,
        isBottomSheetShow = state.isBottomSheetShow,
        directoriesList = state.directoriesList,
        changeBottomSheetShow = {viewModel.onEvent(AddEditAccountEvent.ChangeBottomSheetShow)},
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
        changeAccountDirectory = {viewModel.onEvent(AddEditAccountEvent.ChangeAccountDirectory(it))},
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditAccountPageContent(
    title: String,
    account: Account,
    directory: Directory,
    isPasswordVisible: Boolean,
    isBottomSheetShow: Boolean,
    directoriesList: List<Directory>,
    changeAccountDirectory: (Directory) -> Unit,
    changeBottomSheetShow: () -> Unit,
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

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

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
                        color = if (account.isFavorite) selectedItemColor
                        else onSecondColor)
                ),
                modifier = Modifier.clickable { titleFocus.requestFocus() }
                    .padding(bottom = 1.dp).clip(topRoundedCorner)
            ){
                AddEditTextField(
                    value = account.title,
                    onValueChange = changeAccountTitle,
                    modifier = Modifier.focusRequester(titleFocus)
                )
            }

            AccountInfoWrapper(
                title = stringResource(R.string.choose_directory),
                action = listOf(
                    ActionIcon(iconRes = R.drawable.expand,
                        onClick = {changeBottomSheetShow()})
                ),
                modifier = Modifier.clip(bottomRoundedCorner)
            ){
                DirectoryText(
                    text = directory.name,
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
                    value = account.login,
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
                    value = account.password,
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
                    value = account.siteLink,
                    onValueChange = changeSiteLink,
                    modifier = Modifier.focusRequester(linkFocus)
                )
            }
        }

        if (isBottomSheetShow){
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        changeBottomSheetShow()
                    }
                },
                sheetState = sheetState
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 20.dp)
                        .clip(fullRoundedCorner)
                ) {
                    items(directoriesList){
                        DirectoryContainer(
                            modifier = Modifier.clickable {
                                changeAccountDirectory(it)
                            }.padding(bottom = 1.dp)
                        ) {
                            DirectoryText(
                                text = it.name
                            )
                        }
                    }
                }
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
            title = stringResource( TopBarTitles.EDIT.strId),
            account = ExampleAccount.singleAccount,
            directory = ExampleDirectory.singleDirectory,
            changeAccountDirectory = {},
            isPasswordVisible = true,
            changeAccountTitle = {},
            changeLogin = {},
            changePassword = {},
            changeSiteLink = {},
            changePasswordVisible = {},
            onSave = {_ -> },
            onBack = {},
            changeFavorite = {},
            toGenerator = {},
            isBottomSheetShow = false,
            directoriesList = emptyList(),
            changeBottomSheetShow = {}
        )
    }
}