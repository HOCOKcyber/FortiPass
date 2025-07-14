package com.hocok.fortipass.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.hocok.fortipass.domain.repository.DataStoreRepository
import com.hocok.fortipass.domain.repository.DataStoreRepository.Companion.MASTER_PASSWORD
import kotlinx.coroutines.flow.map


private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "dataStore")

class DataStoreRepositoryImp(val context: Context): DataStoreRepository {

    override val password = context.datastore.data.map { preference ->
        preference[MASTER_PASSWORD] ?: ""
    }

    override suspend fun savePassword(newPassword: String){
        context.datastore.edit { settings ->
            settings[MASTER_PASSWORD] = newPassword
        }
    }

}