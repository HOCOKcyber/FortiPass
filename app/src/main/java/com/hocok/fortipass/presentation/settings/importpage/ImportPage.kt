package com.hocok.fortipass.presentation.settings.importpage

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.settings.components.SettingItem
import com.hocok.fortipass.presentation.settings.util.ImportDataBase
import com.hocok.fortipass.presentation.ui.ActionButton
import com.hocok.fortipass.presentation.ui.TopBarTitles
import com.hocok.fortipass.presentation.ui.components.AppTextField
import com.hocok.fortipass.presentation.ui.components.TopBarComponent
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.actionColor
import com.hocok.fortipass.presentation.ui.theme.secondColor
import com.hocok.fortipass.presentation.ui.theme.selectedItemColor
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@Composable
fun ImportPage(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
){

    val viewModel = hiltViewModel<ImportViewModel>()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val getUriFromStorage = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {result ->
        if (result.resultCode == Activity.RESULT_OK){
            val intent = result.data
            val uri = intent?.data

            if (uri == null) {
                viewModel.onEvent(SettingsImportEvent.ChangeUri(null))
                return@rememberLauncherForActivityResult
            }

            try {
                val contentResolver = context.contentResolver
                val jsonText = readTextFromUri(uri, contentResolver)
                val importDataBase = Json.decodeFromString<ImportDataBase>(jsonText)
                Log.d("import", importDataBase.toString())
                viewModel.onEvent(SettingsImportEvent.ChangeImportData(importDataBase))
                viewModel.onEvent(SettingsImportEvent.ChangeUri(uri))
            } catch (e: RuntimeException){
                viewModel.onEvent(SettingsImportEvent.ChangeUri(null))
                viewModel.onEvent(SettingsImportEvent.MakeErrorToast(R.string.error_import_parse))
            }
        }
    }

    val intentForChooseFile = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/json"
    }

    LaunchedEffect(Unit) {
        viewModel.channelViewEventReceiver.collect {
            Log.d("channel Error", context.getString(it))
            Log.d("channel Error", it.toString())
            Toast.makeText(context, context.getString(it), Toast.LENGTH_SHORT).show()

            if (it == R.string.import_data_success) onBack()
        }
    }

    ImportContent(
        password = state.password,
        uri = state.uri,
        isCrypto = state.isCrypto,
        isImporting = state.isImporting,
        onPasswordChange = {viewModel.onEvent(SettingsImportEvent.ChangePassword(it))},
        onStart = {viewModel.onEvent(SettingsImportEvent.OnStart)},
        chooseFile = {getUriFromStorage.launch(intentForChooseFile)},
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
private fun ImportContent(
    password: String,
    isCrypto: Boolean,
    uri: String,
    onStart: () -> Unit,
    chooseFile: () -> Unit,
    onBack: () -> Unit,
    isImporting: Boolean,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    Scaffold(
        topBar = {
            TopBarComponent(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(TopBarTitles.SETTING_IMPORT.strId),
                back = ActionButton.ActionIcon(iconRes = R.drawable.close, onClick = onBack)
            )
        },
        modifier = modifier.fillMaxSize()
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(10.dp))
            SettingItem(
                title = if (uri.isEmpty()) stringResource(R.string.click_to_choose_file)
                    else "uri: $uri",
                onClick = chooseFile,
                modifier = Modifier.clip(fullRoundedCorner)
            )
            Spacer(Modifier.height(10.dp))
            AnimatedVisibility(
                isCrypto
            ) {
                Box(
                    modifier = Modifier.clip(fullRoundedCorner)
                        .background(secondColor)
                        .fillMaxWidth()
                ) {
                    AppTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        placeholder = stringResource(R.string.enter_old_masster_password),
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp)
                    )
                }
            }
            ImportStartButton(
                onStart = onStart
            )
        }

        if (isImporting){
            DialogForImportWait()
        }
    }
}

@Composable
fun ImportStartButton(
    onStart: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick = onStart,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = actionColor
        ),
        shape = fullRoundedCorner
    ) {
        Text(
            text = stringResource(R.string.start_export),
            color = selectedItemColor,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun DialogForImportWait(){
    Dialog(
        onDismissRequest = {}
    ) {
        Text(
            text = stringResource(R.string.import_wait)
        )
    }
}

@Throws(IOException::class)
private fun readTextFromUri(uri: Uri, contentResolver: ContentResolver): String {
    val stringBuilder = StringBuilder()
    contentResolver.openInputStream(uri)?.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream)).use { reader ->
            var line: String? = reader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = reader.readLine()
            }
        }
    }
    return stringBuilder.toString()
}

@Preview(
    showBackground = true,
)
@Composable
private fun ImportPagePreview(){
    FortiPassTheme {
        ImportContent(
            password = "",
            uri = "",
            isCrypto = true,
            isImporting = false,
            chooseFile = {},
            onStart = {},
            onBack = {},
            onPasswordChange = {},
        )
    }
}