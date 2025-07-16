package com.hocok.fortipass.presentation.authentication.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hocok.fortipass.domain.util.cipher.CipherManager
import com.hocok.fortipass.domain.repository.DataStoreRepository
import com.hocok.fortipass.domain.usecase.PasswordValidation
import com.hocok.fortipass.domain.usecase.RegistrationPasswordValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val dataStoreRep: DataStoreRepository,
): ViewModel() {

    private val validationPassword: RegistrationPasswordValidation = RegistrationPasswordValidation()


    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    private val validationEvent = Channel<PasswordValidation>()
    val validationReceiver = validationEvent.receiveAsFlow()

    fun onEvent(event: RegistrationEvent){
        when(event){
            is RegistrationEvent.ChangePasswordVisibility -> {
                _state.value = _state.value.copy(isPasswordVisibly = !_state.value.isPasswordVisibly)
            }
            is RegistrationEvent.ChangePassword -> {
                _state.value = _state.value.copy(password = event.newPassword)
            }
            is RegistrationEvent.ChangeRepeatedPassword -> {
                _state.value = _state.value.copy(repeatedPassword = event.newRepeatedPassword)
            }
            is RegistrationEvent.ChangeRepeatedPasswordVisibility -> {
                _state.value = _state.value.copy(isRepeatedPasswordVisibly = !_state.value.isRepeatedPasswordVisibly)
            }
            is RegistrationEvent.OnContinue -> {
                val validationResult = validationPassword(_state.value.password, _state.value.repeatedPassword)

                viewModelScope.launch {
                    if (validationResult.isSuccess){
                        val salt = ByteArray(16) { (-127..127).random().toByte() }
                        CipherManager.createHelper(_state.value.password.toByteArray(), salt)
                        val hashPassword = CipherManager.passwordToHash(_state.value.password)

                        dataStoreRep.saveSalt(salt)
                        dataStoreRep.saveHashPassword(hashPassword)

                        validationEvent.send(validationResult)
                    } else {
                        validationEvent.send(validationResult)
                    }
                }
            }
        }
    }
}

data class RegistrationState(
    val password: String = "",
    val isPasswordVisibly: Boolean = false,
    val repeatedPassword: String = "",
    val isRepeatedPasswordVisibly: Boolean = false,
)

sealed class RegistrationEvent{
    data class ChangePassword(val newPassword: String): RegistrationEvent()
    data class ChangeRepeatedPassword(val newRepeatedPassword: String): RegistrationEvent()
    data object ChangePasswordVisibility: RegistrationEvent()
    data object ChangeRepeatedPasswordVisibility: RegistrationEvent()
    data object OnContinue: RegistrationEvent()
}