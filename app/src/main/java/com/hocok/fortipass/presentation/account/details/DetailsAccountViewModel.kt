package com.hocok.fortipass.presentation.account.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.usecase.ChangeFavoriteById
import com.hocok.fortipass.domain.usecase.GetAccountById
import com.hocok.fortipass.domain.usecase.GetDirectoryById
import com.hocok.fortipass.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsAccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getAccountById: GetAccountById,
    val changeFavoriteById: ChangeFavoriteById,
    val getDirectoryById: GetDirectoryById,
): ViewModel() {

    private val id: Int = savedStateHandle.toRoute<Routes.DetailsAccount>().id

    private val _state = MutableStateFlow(DetailsAccountState())
    val state: StateFlow<DetailsAccountState> = _state.asStateFlow()

    fun onEvent(event: DetailsAccountEvent){
        when(event){
            is DetailsAccountEvent.ChangePasswordVisible -> {
                _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
            }
            is DetailsAccountEvent.ChangeFavorite -> {
                viewModelScope.launch {
                    changeFavoriteById(_state.value.account.id!!, !_state.value.account.isFavorite)
                }
            }
        }
    }

    init {
        getAccountById(id)
            .onEach {
                val directory = getDirectoryById(it.idDirectory)
                _state.value = _state.value.copy(
                    account = it,
                    directory = directory
                )
            }.launchIn(viewModelScope)
    }
}

data class DetailsAccountState(
    val account: Account = Account(),
    val directory: Directory = Directory(),
    val isPasswordVisible: Boolean = false,
)

sealed class DetailsAccountEvent(){
    data object ChangePasswordVisible: DetailsAccountEvent()
    data object ChangeFavorite: DetailsAccountEvent()
}
