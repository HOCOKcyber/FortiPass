package com.hocok.fortipass.presentation.account.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.usecase.GetAccountById
import com.hocok.fortipass.domain.usecase.SaveAccount
import com.hocok.fortipass.domain.usecase.Valid
import com.hocok.fortipass.domain.usecase.ValidAccountData
import com.hocok.fortipass.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditAccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val saveAccount: SaveAccount,
    val getAccountById: GetAccountById
): ViewModel() {

    private var _state = MutableStateFlow(AddEditState())
    val state = _state.asStateFlow()

    fun onEvent(event: AddEditAccountEvent){
        when(event){
            is AddEditAccountEvent.ChangePasswordVisible -> {
                _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
            }
            is AddEditAccountEvent.ChangeFavorite -> {
                _state.value = _state.value.copy(account = _state.value.account.copy(isFavorite = !_state.value.account.isFavorite))
            }
            is AddEditAccountEvent.ChangeTitle -> {
                _state.value = _state.value.copy(account = _state.value.account.copy(title = event.newTitle))
            }
            is AddEditAccountEvent.ChangeLogin -> {
                _state.value = _state.value.copy(account = _state.value.account.copy(login = event.newLogin))
            }
            is AddEditAccountEvent.ChangePassword -> {
                _state.value = _state.value.copy(account = _state.value.account.copy(password = event.newPassword))
            }
            is AddEditAccountEvent.ChangeSiteLink -> {
                _state.value = _state.value.copy(account = _state.value.account.copy(siteLink = event.newSiteLink))
            }
            is AddEditAccountEvent.OnSave -> {
                val validationState = ValidAccountData().invoke(_state.value.account)

                if (validationState is Valid.Success) {
                    viewModelScope.launch {
                        saveAccount(account = _state.value.account)
                        event.onBack()
                    }
                }

                event.toastCallBack(validationState.message)
            }
        }
    }

    init {
        val id = savedStateHandle.toRoute<Routes.AddEditAccount>().id

        if (id == null) _state.value = _state.value.copy(account = Account())
        else viewModelScope.launch {
            val account = getAccountById(id).first()
            _state.value = _state.value.copy(account = account)
        }
    }
}

data class AddEditState(
    val account: Account = Account(),
    val isPasswordVisible: Boolean = false,
)

sealed class AddEditAccountEvent{
    data object ChangeFavorite: AddEditAccountEvent()
    data object ChangePasswordVisible: AddEditAccountEvent()
    data class ChangeTitle(val newTitle: String): AddEditAccountEvent()
    data class ChangeLogin(val newLogin: String): AddEditAccountEvent()
    data class ChangePassword(val newPassword: String): AddEditAccountEvent()
    data class ChangeSiteLink(val newSiteLink: String): AddEditAccountEvent()

    data class OnSave(
        val toastCallBack: (message: String) -> Unit,
        val onBack: () -> Unit
    ): AddEditAccountEvent()
}
