package com.hocok.fortipass.presentation.authentication.registration

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.authentication.components.AuthContent
import com.hocok.fortipass.presentation.authentication.components.AuthTextField
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme

@Composable
fun RegistrationPage(
    onContinue: () -> Unit,
){
    val viewModel: RegistrationViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(context) {
        Log.d("Registration", "Prepare to create")
        viewModel.onEvent(RegistrationEvent.CreateFirstFolder(context.getString(R.string.directory_init_name)))
        Log.d("Registration", "Created")
        viewModel.validationReceiver.collect{
            if (it.isSuccess) onContinue()
            else Toast.makeText(context, context.getText(it.errorMessage!!), Toast.LENGTH_SHORT).show()
        }
    }

    RegistrationContent(
        password = state.password,
        repeatedPassword = state.repeatedPassword,
        isShowPassword = state.isPasswordVisibly,
        isShowRepeatedPassword = state.isRepeatedPasswordVisibly,
        onPasswordChange = {viewModel.onEvent(RegistrationEvent.ChangePassword(it))},
        onShowPasswordChange = {viewModel.onEvent(RegistrationEvent.ChangePasswordVisibility)},
        onRepeatedPasswordChange = {viewModel.onEvent(RegistrationEvent.ChangeRepeatedPassword(it))},
        onShowRepeatedPasswordChange = {viewModel.onEvent(RegistrationEvent.ChangeRepeatedPasswordVisibility)},
        onContinue = {viewModel.onEvent(RegistrationEvent.OnContinue)}
    )
}

@Composable
private fun RegistrationContent(
    password: String,
    repeatedPassword: String,
    isShowPassword: Boolean,
    isShowRepeatedPassword: Boolean,
    onPasswordChange: (String) -> Unit,
    onRepeatedPasswordChange: (String) -> Unit,
    onShowPasswordChange: () -> Unit,
    onShowRepeatedPasswordChange: () -> Unit,
    onContinue: () -> Unit,
){

    val repeatPasswordFocus = FocusRequester()

    AuthContent(
        title = stringResource(R.string.registation),
        description = stringResource(R.string.input_password),
        onContinue = onContinue,
    ){
        AuthTextField(
            value = password,
            onValueChange = onPasswordChange,
            isShowValue = isShowPassword,
            onShowValueChange = onShowPasswordChange,
            onDone = {repeatPasswordFocus.requestFocus()}
        )
        Spacer(Modifier.height(5.dp))
        AuthTextField(
            value = repeatedPassword,
            onValueChange = onRepeatedPasswordChange,
            isShowValue = isShowRepeatedPassword,
            onShowValueChange = onShowRepeatedPasswordChange,
            modifier = Modifier.focusRequester(repeatPasswordFocus),
            onDone = onContinue
        )
    }
}

@Preview
@Composable
private fun RegistrationPreview(
){
    FortiPassTheme {
        RegistrationContent(
            password = "",
            repeatedPassword = "",
            isShowPassword = false,
            isShowRepeatedPassword = false,
            onPasswordChange = {},
            onShowPasswordChange = {},
            onRepeatedPasswordChange = {},
            onShowRepeatedPasswordChange = {},
            onContinue = {  }
        )
    }
}