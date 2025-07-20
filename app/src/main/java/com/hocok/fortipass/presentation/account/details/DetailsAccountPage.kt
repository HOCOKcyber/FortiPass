package com.hocok.fortipass.presentation.account.details

import android.content.ClipData
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.model.ExampleAccount
import com.hocok.fortipass.domain.model.ExampleDirectory
import com.hocok.fortipass.domain.model.getInitOrName
import com.hocok.fortipass.presentation.account.components.AccountInfoWrapper
import com.hocok.fortipass.presentation.directory.components.DirectoryText
import com.hocok.fortipass.presentation.ui.ActionButton
import com.hocok.fortipass.presentation.ui.TopBarTitles
import com.hocok.fortipass.presentation.ui.bottomRoundedCorner
import com.hocok.fortipass.presentation.ui.components.DecoratorFloatingButton
import com.hocok.fortipass.presentation.ui.components.TopBarComponent
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.onSecondColor
import com.hocok.fortipass.presentation.ui.theme.secondaryTextColor
import com.hocok.fortipass.presentation.ui.theme.selectedItemColor
import com.hocok.fortipass.presentation.ui.topRoundedCorner

private fun makeCopyToast(context: Context){
    Toast.makeText(context, context.getString(R.string.copied),Toast.LENGTH_SHORT).show()
}

private fun copyTextToClip(text: String): ClipEntry{
    val clipData = ClipData.newPlainText("plain text", text)
    return ClipEntry(clipData)
}

@Composable
fun DetailsAccountPage(
    onBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
){

    val viewModel = hiltViewModel<DetailsAccountViewModel>()
    val state by viewModel.state.collectAsState()

    AccountDetailsPageContent(
        account = state.account,
        directory = state.directory,
        isPasswordVisible = state.isPasswordVisible,
        changePasswordVisible = {viewModel.onEvent(DetailsAccountEvent.ChangePasswordVisible)},
        onBack = onBack,
        onEdit = onEdit,
        modifier = modifier,
    )
}

@Composable
private fun AccountDetailsPageContent(
    account: Account,
    directory: Directory,
    isPasswordVisible: Boolean,
    changePasswordVisible: () -> Unit,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
){
    val clipManager = LocalClipboardManager.current
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopBarComponent(
                title = stringResource( TopBarTitles.DETAILS.strId),
                back = ActionButton.ActionIcon(
                    iconRes = R.drawable.close,
                    onClick = onBack
                )
            )
        },
        floatingActionButton = {
            DecoratorFloatingButton(
                actionIcon = ActionButton.ActionIcon(
                    iconRes = R.drawable.edit,
                    onClick = onEdit
                )
            )
        },
        modifier = modifier
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 20.dp),
        ) {

            Spacer(Modifier.height(10.dp))
            AccountInfoWrapper(
                title = stringResource(R.string.title_account),
                action = listOf(
                    ActionButton.ActionIcon(iconRes = R.drawable.star,
                        onClick = { /*On Details don't need to do nothing*/ },
                        color = if (account.isFavorite) selectedItemColor
                        else onSecondColor
                    )
                ),
                modifier = Modifier.padding(bottom = 1.dp).clip(topRoundedCorner)
            ){
                DetailText(
                    text = account.title
                )
            }

            AccountInfoWrapper(
                title = stringResource(R.string.choose_directory),
                action = listOf(
                    ActionButton.ActionIcon(iconRes = R.drawable.expand, onClick = {
                        /*On Details don't need to do nothing*/
                    })
                ),
                modifier = Modifier.clip(bottomRoundedCorner)
            ){
                DirectoryText(
                    text = directory.getInitOrName(context),
                    style = MaterialTheme.typography.bodyMedium
                )
            }


            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.data_for_authorization),
                style = MaterialTheme.typography.bodyLarge,
                color = secondaryTextColor,
            )
            Spacer(Modifier.height(10.dp))
            AccountInfoWrapper(
                title = stringResource(R.string.name_account),
                action = listOf(ActionButton.ActionIcon(
                    iconRes = R.drawable.copy,
                    onClick = {
                        clipManager.setClip(copyTextToClip(account.login))
                        makeCopyToast(context)
                    })
                ),
                modifier = Modifier.padding(bottom = 1.dp).clip(topRoundedCorner)
            ){
                DetailText(
                    text = account.login,
                )
            }
            AccountInfoWrapper(
                title = stringResource(R.string.password),
                action = listOf(
                    ActionButton.ActionIcon(
                        iconRes =   if (isPasswordVisible) R.drawable.visibility
                        else R.drawable.visibility_off,
                        onClick = changePasswordVisible
                    ),
                    ActionButton.ActionIcon(
                        iconRes = R.drawable.copy,
                        onClick = {
                            clipManager.setClip(copyTextToClip(account.password))
                            makeCopyToast(context)
                        }),
                ),
                modifier = Modifier.clip(bottomRoundedCorner)
            ){
                DetailText(
                    text = account.password,
                    isTextVisible = isPasswordVisible,
                )
            }

            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.options_for_autocompletion),
                style = MaterialTheme.typography.bodyLarge,
                color = secondaryTextColor,
            )
            Spacer(Modifier.height(10.dp))
            AccountInfoWrapper(
                title = stringResource(R.string.site_name),
                action = listOf(
                    ActionButton.ActionIcon(
                        iconRes = R.drawable.gotoweb,
                        onClick = {
                            try {
                                uriHandler.openUri(account.siteLink)
                            } catch (e: Exception){
                                e.message?.let { it1 -> Log.e("Error link", it1) }
                                Toast.makeText(context, context.getString(R.string.wrong_link), Toast.LENGTH_SHORT).show()
                            }
                        })
                ),
                modifier = Modifier.clip(topRoundedCorner).clip(bottomRoundedCorner)
            ){
                DetailText(
                    text = account.siteLink,
                )
            }
        }
    }
}

@Composable
private fun DetailText(
    text: String,
    modifier: Modifier = Modifier,
    isTextVisible: Boolean = true,
){
    SelectionContainer(
        modifier = modifier
    ) {
        Text(
            text = if (isTextVisible) text else "*".repeat(text.length),
            modifier = Modifier,
            color = onSecondColor,
            maxLines = 1,
            overflow = if (isTextVisible) TextOverflow.Ellipsis else TextOverflow.Clip,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
private fun DetailPagePreview(){
    FortiPassTheme {
        AccountDetailsPageContent(
            account = ExampleAccount.singleAccount,
            directory = ExampleDirectory.singleDirectory,
            isPasswordVisible = false,
            changePasswordVisible = {},
            onBack = {},
            onEdit = {},
        )
    }
}