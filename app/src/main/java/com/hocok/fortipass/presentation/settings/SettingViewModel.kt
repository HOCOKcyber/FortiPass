package com.hocok.fortipass.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hocok.fortipass.domain.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    dataStoreRep: DataStoreRepository
): ViewModel(){

    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()

    fun onEvent(event: SettingEvent){
        when(event){
            is SettingEvent.ChangeShowExportDialog -> {
                _state.value = _state.value.copy(isExportDataDialogShow = !_state.value.isExportDataDialogShow)
            }
        }
    }

    init {
        dataStoreRep.uri.onEach {
            _state.value = _state.value.copy(uriExportData = it)
        }.launchIn(viewModelScope)
    }
}


data class SettingState(
    val isExportDataDialogShow: Boolean = false,
    val uriExportData: String = ""
)

sealed class SettingEvent{
    data object ChangeShowExportDialog: SettingEvent()
}