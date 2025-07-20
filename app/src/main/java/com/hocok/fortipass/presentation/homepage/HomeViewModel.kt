package com.hocok.fortipass.presentation.homepage


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.usecase.ChangeFavoriteById
import com.hocok.fortipass.domain.usecase.GetAccounts
import com.hocok.fortipass.domain.usecase.GetDirectories
import com.hocok.fortipass.domain.usecase.SaveDirectory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAccountUseCase: GetAccounts,
    private val changeFavoriteById: ChangeFavoriteById,
    private val getDirectoriesUseCase: GetDirectories,
): ViewModel() {

    private var getAccountJob: Job? = null

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        getAccount(false)
        getDirectories()
    }

    fun onEvent(event: HomeEvent){
        when(event){
            is HomeEvent.ChangeFavoriteDisplay -> {
                _state.value = _state.value.copy(
                    isFavoriteDisplay = !_state.value.isFavoriteDisplay
                )
                getAccount(_state.value.isFavoriteDisplay)
            }
            is HomeEvent.ChangeFavoriteById -> {
                viewModelScope.launch {
                    changeFavoriteById(event.id, !event.isFavorite)
                }
            }
            is HomeEvent.ChangeAccountsDisplay -> {
                _state.value = _state.value.copy(isAccountsDisplay = !_state.value.isAccountsDisplay)
            }
            is HomeEvent.ChangeDialogDisplay -> {
                _state.value = _state.value.copy(isDialogDisplay = !_state.value.isDialogDisplay)
            }
        }
    }

    private fun getAccount(isFavorite: Boolean){
        getAccountJob?.cancel()

        getAccountJob = getAccountUseCase.invoke(isFavorite).onEach {
            _state.value = _state.value.copy(
                accountsList = it
            )
        }.launchIn(viewModelScope)
    }

    private fun getDirectories(){
        getDirectoriesUseCase().onEach {
            Log.d("Home Page", it.toString())
            _state.value = _state.value.copy(directoryList = it)
        }.launchIn(viewModelScope)
    }
}

data class HomeState(
    val accountsList: List<Account> = emptyList(),
    val isFavoriteDisplay: Boolean = false,
    val isAccountsDisplay: Boolean = true,
    val directoryList: List<Directory> = emptyList(),
    val isDialogDisplay: Boolean = false,
)

sealed class HomeEvent{
    data class ChangeFavoriteById(val id: Int, val isFavorite: Boolean): HomeEvent()
    data object ChangeFavoriteDisplay: HomeEvent()
    data object ChangeAccountsDisplay: HomeEvent()
    data object ChangeDialogDisplay: HomeEvent()
}