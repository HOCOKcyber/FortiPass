package com.hocok.fortipass.presentation.authentication.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.authentication.components.AuthContent
import com.hocok.fortipass.presentation.authentication.components.AuthTextField
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme

@Composable
fun LoginPage(
    onContinue: () -> Unit,
){

    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(context) {
        viewModel.validationReceiver.collect{
            if (it) onContinue()
            else Toast.makeText(context, context.getText(R.string.repeat_password), Toast.LENGTH_SHORT).show()
        }
    }

    LoginPageContent(
        password = state.password,
        isShowPassword = state.isPasswordVisibly,
        onPasswordChange = { viewModel.onEvent(LoginEvent.ChangePassword(it)) },
        onPasswordVisibilityChange = { viewModel.onEvent(LoginEvent.ChangePasswordVisibly) },
        onContinue = { viewModel.onEvent(LoginEvent.OnContinue)}
    )
}

@Composable
private fun LoginPageContent(
    password: String,
    onPasswordChange: (String) -> Unit,
    isShowPassword: Boolean,
    onPasswordVisibilityChange: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
){
    AuthContent(
        title = stringResource(R.string.log_in),
        description = stringResource(R.string.input_password),
        onContinue = onContinue,
        modifier = modifier,
    ){
        AuthTextField(
            value = password,
            onValueChange = onPasswordChange,
            isShowValue = isShowPassword,
            onShowValueChange = onPasswordVisibilityChange,
            onDone = onContinue
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
private fun LoginPagePreview(){
    FortiPassTheme {
        LoginPageContent(
            password = "Password",
            isShowPassword = false,
            onPasswordChange = {},
            onPasswordVisibilityChange = {},
            onContinue = {}
        )
    }
}