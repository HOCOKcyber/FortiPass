package com.hocok.fortipass.presentation.generator

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.hocok.fortipass.domain.usecase.CreatePassword
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GeneratorViewModel: ViewModel() {
    private val createPasswordUseCase: CreatePassword = CreatePassword()


    private val _state = MutableStateFlow(GeneratorState())
    val state = _state.asStateFlow()

    private fun createNewPassword(){
        _state.value = _state.value.copy(password =
                if (_state.value.generatorType is GeneratorType.Random)
                    createPasswordUseCase(_state.value.passwordOptions, _state.value.isSimilarSymbolsExclude)
                else
                    createPasswordUseCase(_state.value.mask, _state.value.isSimilarSymbolsExclude)
        )
    }

    fun onEvent(event: GeneratorEvent){
        when(event){
            is GeneratorEvent.ChangeUpperCaseInclude -> {
                _state.value = _state.value.copy(passwordOptions = _state.value.passwordOptions.copy(isUpperCaseInclude = !_state.value.passwordOptions.isUpperCaseInclude))
                createNewPassword()
            }
            is GeneratorEvent.ChangeLowerCaseInclude -> {
                _state.value = _state.value.copy(passwordOptions = _state.value.passwordOptions.copy(isLowerCaseInclude = !_state.value.passwordOptions.isLowerCaseInclude))
                createNewPassword()
            }
            is GeneratorEvent.ChangeNumberInclude -> {
                _state.value = _state.value.copy(passwordOptions = _state.value.passwordOptions.copy(isNumberInclude = !_state.value.passwordOptions.isNumberInclude),)
                createNewPassword()
            }
            is GeneratorEvent.ChangeSymbolsInclude -> {
                _state.value = _state.value.copy(passwordOptions = _state.value.passwordOptions.copy(isSymbolsInclude = !_state.value.passwordOptions.isSymbolsInclude),)
                createNewPassword()
            }
            is GeneratorEvent.ChangeLength -> {
                _state.value = _state.value.copy(passwordOptions = _state.value.passwordOptions.copy(length = event.newLength))
                createNewPassword()
            }

            is GeneratorEvent.ChangeSimilarSymbolsExclude -> {
                _state.value = _state.value.copy(isSimilarSymbolsExclude = !_state.value.isSimilarSymbolsExclude)
                createNewPassword()
            }
            is GeneratorEvent.ChangeMask -> {
                _state.value = _state.value.copy(mask = event.newMask)
                createNewPassword()
            }
            is GeneratorEvent.ChangePassword -> {
                _state.value = _state.value.copy(password = event.newPassword)
            }
            is GeneratorEvent.ChangeGeneratorType -> {
                if (_state.value.generatorType != event.newGeneratorType){
                    _state.value = _state.value.copy(generatorType = event.newGeneratorType)
                }
                createNewPassword()
            }
            is GeneratorEvent.RefreshPassword -> {
                createNewPassword()
            }
        }
    }
}


data class GeneratorState(
    val password: TextFieldValue = TextFieldValue(AnnotatedString("")),
    val generatorType: GeneratorType = GeneratorType.Random,
    val passwordOptions: PasswordOption = PasswordOption(),
    val isSimilarSymbolsExclude: Boolean = false,
    val mask: String = "",
)

data class PasswordOption(
    val length: Float = 8f,
    val isUpperCaseInclude: Boolean = false,
    val isLowerCaseInclude: Boolean = true,
    val isSymbolsInclude: Boolean = false,
    val isNumberInclude: Boolean = false,
)

sealed class GeneratorEvent{
    data object ChangeSymbolsInclude: GeneratorEvent()
    data object ChangeNumberInclude: GeneratorEvent()
    data object ChangeLowerCaseInclude: GeneratorEvent()
    data object ChangeUpperCaseInclude: GeneratorEvent()
    data object ChangeSimilarSymbolsExclude: GeneratorEvent()
    data object RefreshPassword: GeneratorEvent()
    data class ChangeGeneratorType(val newGeneratorType: GeneratorType): GeneratorEvent()
    data class ChangeLength(val newLength: Float): GeneratorEvent()
    data class ChangePassword(val newPassword: TextFieldValue): GeneratorEvent()
    data class ChangeMask(val newMask:String): GeneratorEvent()
}