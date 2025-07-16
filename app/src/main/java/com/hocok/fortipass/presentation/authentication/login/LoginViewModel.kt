package com.hocok.fortipass.presentation.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hocok.fortipass.domain.util.cipher.CipherManager
import com.hocok.fortipass.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRep: DataStoreRepository
): ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val validationEvent = Channel<Boolean>()
    val validationReceiver = validationEvent.receiveAsFlow()

    private var userHashPassword: Int? = null
    private lateinit var salt: ByteArray

    fun onEvent(event: LoginEvent){

        when(event){
            is LoginEvent.ChangePasswordVisibly -> {
                _state.value = _state.value.copy(isPasswordVisibly = !_state.value.isPasswordVisibly)
            }
            is LoginEvent.ChangePassword -> {
                _state.value = _state.value.copy(password = event.newPassword)
            }
            is LoginEvent.OnContinue -> {
                viewModelScope.launch {
                    val isValid = CipherManager.checkPassword(_state.value.password, userHashPassword!!)
                    if (isValid){
                        CipherManager.createHelper(_state.value.password.toByteArray(), salt)
                        val data = CipherManager.encrypt("Hello world")
                        CipherManager.decrypt(data.first, data.second)
                    }
                    validationEvent.send(isValid)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            userHashPassword = dataStoreRep.hashPassword.first()
            salt = dataStoreRep.salt.first()
        }
    }
}

data class LoginState(
    val password: String = "",
    val isPasswordVisibly: Boolean = false,
)

sealed class LoginEvent{
    data object ChangePasswordVisibly: LoginEvent()
    data class ChangePassword(val newPassword: String): LoginEvent()
    data object OnContinue: LoginEvent()
}