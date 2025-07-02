package com.hocok.fortipass.presentation.directory.addedit

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.directory.components.DirectoryContainer
import com.hocok.fortipass.presentation.directory.components.DirectoryTextField
import com.hocok.fortipass.presentation.ui.ActionIcon
import com.hocok.fortipass.presentation.ui.TopBarTitles
import com.hocok.fortipass.presentation.ui.components.TopBarComponent
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme

@Composable
fun AddEditDirectoryPage(
    title: TopBarTitles,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
){
    val viewModel: AddEditDirectoryViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    AddEditDirectoryPageContent(
        title = title,
        directoryName = state.directory.name,
        changeDirectoryName = {viewModel.onEvent(AddEditDirectoryEvent.ChangeName(it))},
        onBack = onBack,
        onSave = {viewModel.onEvent(AddEditDirectoryEvent.Save(toastCallBack = it, onBack = onBack))},
        modifier = modifier,
    )
}

@Composable
private fun AddEditDirectoryPageContent(
    title: TopBarTitles,
    directoryName: String,
    changeDirectoryName: (String) -> Unit,
    onBack: () -> Unit,
    onSave: (toastCallBack: (String) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
){
    val context = LocalContext.current

    val folderTextField = FocusRequester()

    Scaffold(
        topBar = {
            TopBarComponent(
                modifier = Modifier.fillMaxWidth(),
                title = title,
                action = listOf(
                    ActionIcon(iconRes = R.drawable.done, onClick = {
                        onSave{ Toast.makeText(context, it, Toast.LENGTH_LONG).show() }
                    })
                ),
                back = ActionIcon(iconRes = R.drawable.close, onClick = onBack)
            )
        },

        modifier = modifier.fillMaxSize()
    ) { contentPadding ->

        DirectoryContainer(
            modifier = Modifier.padding(contentPadding)
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .clip(fullRoundedCorner)
                .clickable { folderTextField.requestFocus() }
        ) {
            DirectoryTextField(
                value = directoryName,
                onValueChange = changeDirectoryName,
                modifier.focusRequester(folderTextField)
            )
        }
    }
}

@Preview
@Composable
private fun AddEditDirectoryPagePreview(){
    FortiPassTheme {
        AddEditDirectoryPageContent(
            title = TopBarTitles.EDIT,
            directoryName = "exampleName",
            changeDirectoryName = {},
            onBack = {},
            onSave = {},
        )
    }
}