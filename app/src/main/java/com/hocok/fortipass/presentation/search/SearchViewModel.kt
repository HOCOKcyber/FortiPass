package com.hocok.fortipass.presentation.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.usecase.ChangeFavoriteById
import com.hocok.fortipass.domain.usecase.SearchAccounts
import com.hocok.fortipass.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val searchAccounts: SearchAccounts,
    private val changeFavoriteById: ChangeFavoriteById,
): ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private var searchJob: Job? =  null

    fun onEvent(event: SearchEvent){
        when(event){
            is SearchEvent.ChangeRequest -> {
                _state.value = _state.value.copy(request = event.newRequest)
                search()
            }
            is SearchEvent.ChangeFavoriteById -> {
                viewModelScope.launch {
                    changeFavoriteById(event.id, !event.isFavorite)
                }
            }
        }
    }

    private fun search(){
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(300)
            searchAccounts(_state.value.request, _state.value.directoryName).collect {
                _state.value = _state.value.copy(
                    listAccount = it
                )
            }
        }
    }

    init {
        _state.value = _state.value.copy(
            directoryName = savedStateHandle.toRoute<Routes.Search>().directoryName
        )
        search()
    }

}


data class SearchState(
    val request: String = "",
    val directoryName: String? = "",
    val listAccount: List<Account> = emptyList(),
)

sealed class SearchEvent{
    data class ChangeRequest(val newRequest: String): SearchEvent()
    data class ChangeFavoriteById(val id: Int, val isFavorite: Boolean): SearchEvent()
}