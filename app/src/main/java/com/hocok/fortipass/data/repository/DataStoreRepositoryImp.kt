package com.hocok.fortipass.data.repository

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.hocok.fortipass.domain.repository.DataStoreRepository
import com.hocok.fortipass.domain.repository.DataStoreRepository.Companion.HASH_MASTER_PASSWORD
import com.hocok.fortipass.domain.repository.DataStoreRepository.Companion.SALT
import com.hocok.fortipass.domain.repository.DataStoreRepository.Companion.URI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "dataStore")

class DataStoreRepositoryImp(private val context: Context): DataStoreRepository {

    override val hashPassword = context.datastore.data.map { preference ->
        preference[HASH_MASTER_PASSWORD]
    }

    override val salt: Flow<ByteArray> = context.datastore.data.map { preference ->
        preference[SALT] ?: ByteArray(1)
    }

    override val uri: Flow<String> = context.datastore.data.map { preference ->
        preference[URI] ?: ""
    }

    override suspend fun saveHashPassword(hashPassword: Int){
        context.datastore.edit { settings ->
            settings[HASH_MASTER_PASSWORD] = hashPassword
        }
    }

    override suspend fun saveSalt(salt: ByteArray) {
        context.datastore.edit { settings ->
            settings[SALT] = salt
        }
    }

    override suspend fun saveUri(uri: Uri){
        context.datastore.edit { settings ->
            settings[URI] = uri.toString()
        }
    }

}