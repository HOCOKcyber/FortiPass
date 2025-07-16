package com.hocok.fortipass.presentation.account.addedit

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.model.getDecodeAccount
import com.hocok.fortipass.domain.model.getEncodeAccount
import com.hocok.fortipass.domain.usecase.GetAccountById
import com.hocok.fortipass.domain.usecase.GetDirectories
import com.hocok.fortipass.domain.usecase.GetDirectoryById
import com.hocok.fortipass.domain.usecase.SaveAccount
import com.hocok.fortipass.domain.usecase.Valid
import com.hocok.fortipass.domain.usecase.ValidAccountData
import com.hocok.fortipass.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditAccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val saveAccount: SaveAccount,
    val getAccountById: GetAccountById,
    getDirectories: GetDirectories,
    val getDirectoryById: GetDirectoryById,
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
                        saveAccount(account = _state.value.account.getEncodeAccount())
                        event.onBack()
                    }
                }

                event.toastCallBack(validationState.message)
            }
            is AddEditAccountEvent.ChangeBottomSheetShow -> {
                _state.value = _state.value.copy(isBottomSheetShow = !_state.value.isBottomSheetShow)
            }
            is AddEditAccountEvent.ChangeAccountDirectory -> {
                _state.value = _state.value.copy(
                    isBottomSheetShow = false,
                    currentDirectory = event.newDirectory,
                    account = _state.value.account.copy(
                        idDirectory = event.newDirectory.id
                    )
                )
            }
        }
    }

    init {
        val id = savedStateHandle.toRoute<Routes.AddEditAccount>().id

        viewModelScope.launch {
            if (id != null){
                    val account = getAccountById(id).first().getDecodeAccount()
                    val directory = getDirectoryById(account.idDirectory!!)
                    _state.value = _state.value.copy(
                        account = account,
                        currentDirectory = directory
                    )
            } else {
                _state.value = _state.value.copy(
                    currentDirectory = getDirectoryById(0)
                )
            }
        }

        getDirectories().onEach {
            Log.d("getDirectory:", it.toString() )
            _state.value = _state.value.copy(directoriesList = it)
        }.launchIn(viewModelScope)
    }
}

data class AddEditState(
    val account: Account = Account(),
    val directoriesList: List<Directory> = emptyList(),
    val currentDirectory: Directory = Directory(),
    val isPasswordVisible: Boolean = false,
    val isBottomSheetShow: Boolean = false,
)

sealed class AddEditAccountEvent{
    data object ChangeFavorite: AddEditAccountEvent()
    data object ChangePasswordVisible: AddEditAccountEvent()
    data object ChangeBottomSheetShow: AddEditAccountEvent()
    data class ChangeAccountDirectory(val newDirectory: Directory): AddEditAccountEvent()
    data class ChangeTitle(val newTitle: String): AddEditAccountEvent()
    data class ChangeLogin(val newLogin: String): AddEditAccountEvent()
    data class ChangePassword(val newPassword: String): AddEditAccountEvent()
    data class ChangeSiteLink(val newSiteLink: String): AddEditAccountEvent()

    data class OnSave(
        val toastCallBack: (message: String) -> Unit,
        val onBack: () -> Unit
    ): AddEditAccountEvent()
}
