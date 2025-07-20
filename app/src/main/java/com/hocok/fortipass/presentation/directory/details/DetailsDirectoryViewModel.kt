package com.hocok.fortipass.presentation.directory.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.usecase.GetAccountsByDirectoryName
import com.hocok.fortipass.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsDirectoryViewModel @Inject  constructor(
    savedStateHandle: SavedStateHandle,
    val getAccountsByDirectoryName: GetAccountsByDirectoryName
): ViewModel(){

    private val _state = MutableStateFlow(DetailsDirectoryState())
    val state = _state.asStateFlow()

    init {
        val nameDirectory = savedStateHandle.toRoute<Routes.DetailsDirectory>().nameDirectory

        viewModelScope.launch {
            val accountList = getAccountsByDirectoryName(nameDirectory)
            _state.value = _state.value.copy(
                accountsList = accountList,
                directory = Directory(name = nameDirectory)
            )
        }
    }
}

data class DetailsDirectoryState(
    val accountsList: List<Account> = emptyList(),
    val directory: Directory = Directory()
)

