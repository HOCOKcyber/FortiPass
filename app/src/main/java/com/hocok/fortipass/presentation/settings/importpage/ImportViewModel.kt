package com.hocok.fortipass.presentation.settings.importpage

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hocok.fortipass.R
import com.hocok.fortipass.domain.repository.AccountRepository
import com.hocok.fortipass.domain.util.cipher.CipherManager
import com.hocok.fortipass.presentation.settings.util.ImportDataBase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.crypto.AEADBadTagException
import javax.inject.Inject

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val dbRep: AccountRepository,
): ViewModel() {

    private val _state = MutableStateFlow(SettingsImportState())
    val state = _state.asStateFlow()

    private val channelViewEvent = Channel<Int>()
    val channelViewEventReceiver = channelViewEvent.receiveAsFlow()

    fun onEvent(event: SettingsImportEvent){
        when(event){
            is SettingsImportEvent.ChangePassword -> {
                _state.value = _state.value.copy(password = event.newPassword)
            }
            is SettingsImportEvent.OnStart -> {
                _state.value = _state.value.copy(isImporting = true)
                saveInDatabase()
            }
            is SettingsImportEvent.ChangeUri -> {
                val uri = event.uri?.toString() ?: ""

                _state.value = _state.value.copy(
                    uri = uri,
                    isCrypto = if (uri.isEmpty()) false else _state.value.isCrypto
                )
            }
            is SettingsImportEvent.ChangeImportData -> {
                _state.value = _state.value.copy(
                    importData = event.newData,
                    isCrypto = event.newData.salt.isNotEmpty()
                )
            }
            is SettingsImportEvent.MakeErrorToast -> {
                viewModelScope.launch {
                    channelViewEvent.send(event.errorId)
                }
            }
        }
    }


    private fun saveInDatabase(){
        viewModelScope.launch {
            val importData = _state.value.importData
            if (_state.value.isCrypto) CipherManager.createHelper(_state.value.password.toByteArray(), importData.salt, false)
            try {
                for (account in importData.accounts){
                    val initPassword = if (_state.value.isCrypto) CipherManager.decrypt(account.password, account.iv, CipherManager.tempCipherHelper)
                                    else account.password

                    val (newPassword, newIv) = CipherManager.encrypt(initPassword)
                    dbRep.insertAccount(
                        account.copy(
                            id = null,
                            password = newPassword,
                            iv = newIv,
                        )
                    )
                }
                for (directory in importData.directory){
                    dbRep.saveDirectory(directory.copy(
                        id = null
                    ))
                }
                channelViewEvent.send(R.string.import_data_success)
            } catch (e: AEADBadTagException){
                channelViewEvent.send(R.string.error_wrong_import_password)
            } catch (e: Exception){
                channelViewEvent.send(R.string.error_unknown)
            } finally {
                _state.value = _state.value.copy(isImporting = false)
            }
        }
    }

}

data class SettingsImportState(
    val password: String = "",
    val isCrypto: Boolean = false,
    val uri: String = "",
    val errorMessage: String = "",
    val isImporting: Boolean = false,
    val importData: ImportDataBase = ImportDataBase()
)

sealed class SettingsImportEvent(){
    data object OnStart: SettingsImportEvent()

    data class ChangePassword(val newPassword: String): SettingsImportEvent()
    data class ChangeUri(val uri: Uri?): SettingsImportEvent()
    data class ChangeImportData(val newData: ImportDataBase): SettingsImportEvent()
    data class MakeErrorToast(val errorId: Int): SettingsImportEvent()
}