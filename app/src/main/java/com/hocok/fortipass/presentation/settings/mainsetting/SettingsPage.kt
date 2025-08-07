package com.hocok.fortipass.presentation.settings.mainsetting

import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.MainActivity
import com.hocok.fortipass.presentation.settings.components.SettingItem
import com.hocok.fortipass.presentation.ui.ActionButton
import com.hocok.fortipass.presentation.ui.TopBarTitles
import com.hocok.fortipass.presentation.ui.components.FortiPassDialog
import com.hocok.fortipass.presentation.ui.components.TopBarComponent
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.secondColor
import androidx.core.net.toUri

@Composable
fun SettingsPage(
    toImport: () -> Unit,
){

    val viewModel = hiltViewModel<SettingViewModel>()
    val state by viewModel.state.collectAsState()

    val context = LocalActivity.current as MainActivity
    
    SettingsPageContent(
        state = state,
        changeShowExportDialog = {viewModel.onEvent(SettingEvent.ChangeShowExportDialog)},
        exportData = { context.selectFile(it) },
        openFile = { openExportData(context, state.uriExportData) },
        toImport = toImport,
    )
}

@Composable
private fun SettingsPageContent(
    state: SettingState,
    changeShowExportDialog: () -> Unit,
    exportData: (Boolean) -> Unit,
    openFile: () -> Unit,
    toImport: () -> Unit,
    modifier: Modifier = Modifier
){

    Scaffold(
        topBar = {
            TopBarComponent(
                title = stringResource( TopBarTitles.SETTING.strId ),
            )
        }
    ) {innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.padding(5.dp))
            SettingSection(
                title = stringResource(R.string.export_data)
            ) {
                SettingItem(
                    title = stringResource(R.string.export),
                    onClick = changeShowExportDialog
                )
                SettingItem(
                    title = stringResource(R.string.go_to_file),
                    onClick = openFile
                )
                SettingItem(
                    title = stringResource(R.string.import_title),
                    onClick = toImport
                )
            }
        }

        if (state.isExportDataDialogShow){
            FortiPassDialog(
                onDismissRequest = changeShowExportDialog,
                title = stringResource(R.string.choose_type_export_data),
                actionList = listOf(
                    ActionButton.ActionText(
                        textRes = R.string.cipher_type_export_data,
                        onClick = {
                            changeShowExportDialog()
                            exportData(true)
                        }
                    ),
                    ActionButton.ActionText(
                        textRes = R.string.open_type_export_data,
                        onClick = {
                            changeShowExportDialog()
                            exportData(false)
                        }
                    )
                )
            )
        }

        }
}


@Composable
private fun SettingSection(
    title: String,
    content: @Composable () -> Unit
){
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        color = secondColor
    )
    Spacer(Modifier.height(10.dp))
    Column(
        modifier =  Modifier
            .clip(fullRoundedCorner)
    ){
        content()
    }
    Spacer(Modifier.height(10.dp))
}


private fun openExportData(activity: MainActivity, strUri: String){
    if (strUri.isEmpty()) return

    val uri = strUri.toUri()
    activity.openFile(uri)
}

@Preview(
    showBackground = true,
)
@Composable
private fun SettingsPagePreview(){
    FortiPassTheme {
        SettingsPageContent(
            state = SettingState(),
            changeShowExportDialog = {},
            exportData = {},
            openFile = {},
            toImport = {},
        )
    }
}
