package com.hocok.fortipass.presentation.directory.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.usecase.SaveDirectory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditDirectoryViewModel @Inject constructor(
    val saveDirectoryUseCase: SaveDirectory
): ViewModel() {

    private val _state = MutableStateFlow(AddEditDirectoryState())
    val state = _state.asStateFlow()

    fun onEvent(event: AddEditDirectoryEvent){
        when(event){
            is AddEditDirectoryEvent.Save -> {

                if(validDirectoryName(_state.value.directory.name)){
                    viewModelScope.launch {
                        saveDirectoryUseCase(_state.value.directory)
                        event.toastCallBack("Сохранено")
                        event.onBack()
                    }
                } else {
                    event.toastCallBack("Имя пустое")
                }
            }
            is AddEditDirectoryEvent.ChangeName -> {
                _state.value = _state.value.copy(directory = _state.value.directory.copy(name = event.newName))
            }
        }
    }

    private fun validDirectoryName(name: String): Boolean{
        return name.isNotEmpty()
    }
}

data class AddEditDirectoryState(
    val directory: Directory = Directory()
)

sealed class AddEditDirectoryEvent(){
    data class ChangeName(val newName: String): AddEditDirectoryEvent()
    data class Save(val toastCallBack: (String) -> Unit, val onBack: () -> Unit): AddEditDirectoryEvent()

}