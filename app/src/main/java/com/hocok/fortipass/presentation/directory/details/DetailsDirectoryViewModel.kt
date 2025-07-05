package com.hocok.fortipass.presentation.directory.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.usecase.GetAccountsByDirectoryId
import com.hocok.fortipass.domain.usecase.GetDirectoryById
import com.hocok.fortipass.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsDirectoryViewModel @Inject  constructor(
    savedStateHandle: SavedStateHandle,
    val getAccountsByDirectoryId: GetAccountsByDirectoryId,
    val getDirectoryById: GetDirectoryById,
): ViewModel(){

    private val _state = MutableStateFlow(DetailsDirectoryState())
    val state = _state.asStateFlow()

    init {
        val idDirectory = savedStateHandle.toRoute<Routes.DetailsDirectory>().idDirectory

        viewModelScope.launch {
            val accountList = getAccountsByDirectoryId(idDirectory)
            val directory = getDirectoryById(idDirectory)
            _state.value = _state.value.copy(
                accountsList = accountList,
                directory = directory
            )
        }
    }
}

data class DetailsDirectoryState(
    val accountsList: List<Account> = emptyList(),
    val directory: Directory = Directory()
)

