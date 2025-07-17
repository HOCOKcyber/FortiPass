package com.hocok.fortipass.presentation.generator

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.ui.ActionButton
import com.hocok.fortipass.presentation.ui.TopBarTitles
import com.hocok.fortipass.presentation.ui.bottomRoundedCorner
import com.hocok.fortipass.presentation.ui.components.CustomSwitchButton
import com.hocok.fortipass.presentation.ui.components.TopBarComponent
import com.hocok.fortipass.presentation.ui.fullRoundedCorner
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.actionColor
import com.hocok.fortipass.presentation.ui.theme.blueForNumberColor
import com.hocok.fortipass.presentation.ui.theme.mainTextColor
import com.hocok.fortipass.presentation.ui.theme.onSecondColor
import com.hocok.fortipass.presentation.ui.theme.redForSymbolColor
import com.hocok.fortipass.presentation.ui.theme.secondColor
import com.hocok.fortipass.presentation.ui.topRoundedCorner

sealed class GeneratorType{
    data object Random: GeneratorType()
    data object Mask: GeneratorType()
}

@Composable
fun GeneratorPage(
    onBack: () -> Unit,
    isFromAddEdit: Boolean,
    savePasswordFromGenerator: (String) -> Unit,
    modifier: Modifier = Modifier,
){
    val viewModel = hiltViewModel<GeneratorViewModel>()
    val state by viewModel.state.collectAsState()

    GeneratorPageContent(
        password = state.password,
        length = state.passwordOptions.length,
        isUpperCaseInclude = state.passwordOptions.isUpperCaseInclude,
        isLowerCaseInclude = state.passwordOptions.isLowerCaseInclude,
        isSymbolsInclude= state.passwordOptions.isSymbolsInclude,
        isNumberInclude= state.passwordOptions.isNumberInclude,
        isSimilarSymbolsExclude = state.isSimilarSymbolsExclude,
        maskValue = state.mask,
        generatorType = state.generatorType,
        isFromAddEdit = isFromAddEdit,
        passwordChange = {viewModel.onEvent(GeneratorEvent.ChangePassword(it))},
        changeMask = {viewModel.onEvent(GeneratorEvent.ChangeMask(it))},
        lengthChange = {viewModel.onEvent(GeneratorEvent.ChangeLength(it))},
        changeSymbolsInclude = {viewModel.onEvent(GeneratorEvent.ChangeSymbolsInclude)},
        changeNumberInclude = {viewModel.onEvent(GeneratorEvent.ChangeNumberInclude)},
        changeLowerCaseInclude = {viewModel.onEvent(GeneratorEvent.ChangeLowerCaseInclude)},
        changeUpperCaseInclude = {viewModel.onEvent(GeneratorEvent.ChangeUpperCaseInclude)},
        changeSimilarSymbolsExclude = {viewModel.onEvent(GeneratorEvent.ChangeSimilarSymbolsExclude)},
        changeGeneratorType = {viewModel.onEvent(GeneratorEvent.ChangeGeneratorType(it))},
        refreshPassword = {viewModel.onEvent(GeneratorEvent.RefreshPassword)},
        onBack = onBack,
        savePasswordFromGenerator = savePasswordFromGenerator,
        modifier = modifier
    )
}

@Composable
private fun GeneratorPageContent(
    length: Float,
    maskValue: String,
    generatorType: GeneratorType,
    password: TextFieldValue,
    isUpperCaseInclude: Boolean,
    isLowerCaseInclude: Boolean,
    isSymbolsInclude: Boolean,
    isNumberInclude: Boolean,
    isSimilarSymbolsExclude: Boolean,
    isFromAddEdit: Boolean,
    savePasswordFromGenerator: (String) -> Unit,
    changeGeneratorType: (GeneratorType) -> Unit,
    changeMask: (String) -> Unit,
    passwordChange: (TextFieldValue) -> Unit,
    onBack: () -> Unit,
    refreshPassword: () -> Unit,
    lengthChange: (Float) -> Unit,
    changeUpperCaseInclude: (Boolean) -> Unit,
    changeLowerCaseInclude: (Boolean) -> Unit,
    changeSymbolsInclude: (Boolean) -> Unit,
    changeNumberInclude: (Boolean) -> Unit,
    changeSimilarSymbolsExclude: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
){
    val clipManager = LocalClipboardManager.current
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBarComponent(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource( TopBarTitles.GENERATOR.strId),
                back = ActionButton.ActionIcon(
                    iconRes = R.drawable.close,
                    onClick = onBack,
                ),
                action =
                    if (isFromAddEdit) listOf(ActionButton.ActionIcon(
                        iconRes = R.drawable.done,
                        onClick = {savePasswordFromGenerator(password.text)}
                    )) else emptyList<ActionButton.ActionIcon>()

            )
        },
    ){ contentPadding ->
       Column(
           modifier = modifier
               .padding(contentPadding)
               .padding(horizontal = 20.dp)
       ) {
           ChooseTypePassword(
               generatorType = generatorType,
               changeGeneratorType = changeGeneratorType,
           )
           Spacer(Modifier.height(10.dp))
           GeneratorPasswordField(
               password = password,
               onPasswordChange = passwordChange,
               refreshPassword = ActionButton.ActionIcon(
                   iconRes = R.drawable.lock,
                   onClick = refreshPassword
               ),
               modifier = Modifier.fillMaxWidth()
           )
           Spacer(Modifier.height(10.dp))
           Button(
               onClick = {
                   val clitData = ClipData.newPlainText("plain Text", password.text)
                   val clipEntry = ClipEntry(clitData)
                   clipManager.setClip(clipEntry)
                   Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show()
               },
               modifier = Modifier.fillMaxWidth()
           ) {
               Text(
                   text = stringResource(R.string.copy),
                   style = MaterialTheme.typography.bodyLarge,
                   color = secondColor
               )
           }
           Spacer(Modifier.height(10.dp))
           when(generatorType){
               is GeneratorType.Random -> {
                   PasswordOptionSection(
                       length = length,
                       lengthChange = lengthChange,
                       isUpperCaseInclude = isUpperCaseInclude,
                       isLowerCaseInclude = isLowerCaseInclude,
                       isSymbolsInclude= isSymbolsInclude,
                       isNumberInclude= isNumberInclude,
                       changeSymbolsInclude = changeSymbolsInclude,
                       changeNumberInclude = changeNumberInclude,
                       changeLowerCaseInclude = changeLowerCaseInclude,
                       changeUpperCaseInclude = changeUpperCaseInclude,
                   )
               }
               is GeneratorType.Mask -> {
                   PasswordInstructionsSection(
                       maskValue = maskValue,
                       changeMask = changeMask,
                   )
               }
           }


           Spacer(Modifier.height(10.dp))
           PasswordOption(
               optionName = stringResource(R.string.exclude_similar_symbols),
               isChecked = isSimilarSymbolsExclude,
               onCheckedChange = changeSimilarSymbolsExclude,
               shape = fullRoundedCorner
           )
       }
    }
}

@Composable
private fun ChooseTypePassword(
    generatorType: GeneratorType,
    changeGeneratorType: (GeneratorType) -> Unit,
    modifier: Modifier = Modifier,
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(fullRoundedCorner)
            .background(secondColor)
    ){
        Row(
            modifier = Modifier
                .padding(2.dp)
                .height(25.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            ChooserText(
                text = stringResource(R.string.random_password),
                isSelected = generatorType is GeneratorType.Random,
                modifier = Modifier.weight(1f).clickable {
                    changeGeneratorType(GeneratorType.Random)
                }
            )
            ChooserText(
                text = stringResource(R.string.mask_password),
                isSelected = generatorType is GeneratorType.Mask,
                modifier = Modifier.weight(1f).clickable {
                    changeGeneratorType(GeneratorType.Mask)
                }
            )
        }
    }
}

@Composable
private fun ChooserText(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
){

    val newModifier = if (isSelected) modifier
        .clip(RoundedCornerShape(8.dp))
        .background(actionColor)
    else modifier

    Box(
        modifier = newModifier.fillMaxSize()
    ){
        Text(
            text = text,
            color = if (isSelected) secondColor else mainTextColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun GeneratorPasswordField(
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    refreshPassword: ActionButton.ActionIcon? = null,
){
    val textFieldFocus = FocusRequester()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(fullRoundedCorner)
            .background(secondColor)
            .clickable { textFieldFocus.requestFocus() }
    ){
        BasicTextField(
            value = password,
            onValueChange = onPasswordChange,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .focusRequester(textFieldFocus),
            textStyle = MaterialTheme.typography.bodyMedium,
            cursorBrush = Brush.verticalGradient(
                colors = listOf(actionColor, actionColor)
            )
        )

        refreshPassword?.let {
            IconButton(
                onClick = refreshPassword.onClick,
            ) {
                Icon(
                    painter = painterResource(refreshPassword.iconRes),
                    contentDescription = null,
                    tint = mainTextColor
                )
            }
        }
    }
}

@Composable
private fun PasswordLength(
    length: Float,
    lengthChange: (Float) -> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(secondColor)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(end = 15.dp)
            ) {
                Text(
                    text = stringResource(R.string.length),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 5.dp)
                )
                Text(
                    text = length.toInt().toString(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Slider(
                value = length,
                onValueChange = lengthChange,
                valueRange = 8f..100f,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PasswordOption(
    optionName: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp)
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(secondColor)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 7.dp, horizontal = 12.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = optionName,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                modifier = Modifier.weight(1f)
            )
            CustomSwitchButton(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                switchPadding = 6.dp,
                buttonWidth = 50.dp,
                buttonHeight = 25.dp,
                thumbColor = actionColor,
                trackColor = onSecondColor,
            )
        }
    }
}

@Composable
private fun PasswordOptionSection(
    length: Float,
    lengthChange: (Float) -> Unit,
    isUpperCaseInclude: Boolean,
    isLowerCaseInclude: Boolean,
    isSymbolsInclude: Boolean,
    isNumberInclude: Boolean,
    changeUpperCaseInclude: (Boolean) -> Unit,
    changeLowerCaseInclude: (Boolean) -> Unit,
    changeSymbolsInclude: (Boolean) -> Unit,
    changeNumberInclude: (Boolean) -> Unit,
){
    PasswordLength(
        length = length,
        lengthChange = lengthChange,
        modifier = Modifier,
    )
    Spacer(Modifier.height(10.dp))
    PasswordOption(
        optionName = "A-Z",
        isChecked = isUpperCaseInclude,
        onCheckedChange = changeUpperCaseInclude,
        shape = topRoundedCorner,
        modifier = Modifier.padding(bottom = 1.dp)
    )
    PasswordOption(
        optionName = "a-z",
        isChecked = isLowerCaseInclude,
        onCheckedChange = changeLowerCaseInclude,
        modifier = Modifier.padding(bottom = 1.dp)
    )
    PasswordOption(
        optionName = "0-9",
        isChecked = isNumberInclude,
        onCheckedChange = changeNumberInclude,
        modifier = Modifier.padding(bottom = 1.dp)
    )
    PasswordOption(
        optionName = "!@#\$%^&*",
        isChecked = isSymbolsInclude,
        onCheckedChange = changeSymbolsInclude,
        shape = bottomRoundedCorner,
        modifier = Modifier.padding(bottom = 1.dp)
    )
}

@Composable
private fun PasswordInstructionsSection(
    maskValue: String,
    changeMask: (String) -> Unit,
){
    Box(
        modifier = Modifier
            .clip(fullRoundedCorner)
            .background(secondColor)
            .fillMaxWidth()
    ){
        BasicTextField(
            value = maskValue,
            onValueChange = changeMask,
            textStyle = MaterialTheme.typography.bodyMedium,
            cursorBrush = Brush.verticalGradient(
                colors = listOf(actionColor, actionColor)
            ),
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
        ){innerTextField ->
            Box{
                if (maskValue.isEmpty()){
                    Text(
                        text = stringResource(R.string.start_enter_mask),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                innerTextField()
            }
        }
    }
    Spacer(Modifier.height(10.dp))

    val modifierBottomPadding = Modifier.padding(bottom = 1.dp)

    PasswordInstruction(
        instruction = "X - ${stringResource(R.string.upper_case)} (A-Z)",
        shape = topRoundedCorner,
        modifier = modifierBottomPadding
    )
    PasswordInstruction(
        instruction = "x - ${stringResource(R.string.lower_case)} (a-z)",
        modifier = modifierBottomPadding)
    PasswordInstruction(
        instruction = "# - ${stringResource(R.string.numbers)} (0-9)",
        modifier = modifierBottomPadding
    )
    PasswordInstruction(
        instruction = "S - ${stringResource(R.string.symbols)} (!@#\$%^&*)",
        modifier = modifierBottomPadding
    )
    PasswordInstruction(
        instruction = "R - ${stringResource(R.string.random_not_special)} (A-Z, a-z, 0-9)",
        modifier = modifierBottomPadding
    )
    PasswordInstruction(
        instruction = "/../ - ${stringResource(R.string.fixed_set)}",
        shape = bottomRoundedCorner
    )
}

@Composable
private fun PasswordInstruction(
    instruction: String,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp)
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(secondColor)
    ){
        Text(
            text = instruction,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Preview
@Composable
private fun GeneratorPagePreview() {
    val passwordText = "qwerty123$"
    val styledPasswordText = buildAnnotatedString {
        passwordText.forEachIndexed{ i, s ->
            when(s) {
                in "qwerty" -> addStyle(style = SpanStyle(mainTextColor), start = i, end = i+1)
                in "123" -> addStyle(style = SpanStyle(blueForNumberColor), start = i, end = i+1)
                in "$" -> addStyle(style = SpanStyle(redForSymbolColor), start = i, end = i+1)
            }
            append(s)
        }
    }
    FortiPassTheme {
        GeneratorPageContent(
            password = TextFieldValue(styledPasswordText),
            passwordChange = {},
            onBack = {},
            length = 80f,
            lengthChange = {},
            maskValue = "xxx",
            isUpperCaseInclude = false,
            isLowerCaseInclude = true,
            isSymbolsInclude= false,
            isNumberInclude= true,
            isSimilarSymbolsExclude = false,
            changeSymbolsInclude = {},
            changeNumberInclude = {},
            changeLowerCaseInclude = {},
            changeUpperCaseInclude = {},
            changeSimilarSymbolsExclude = {},
            changeMask = {},
            changeGeneratorType = {},
            generatorType = GeneratorType.Mask,
            refreshPassword = {},
            isFromAddEdit = false,
            savePasswordFromGenerator = {}
        )
    }
}
