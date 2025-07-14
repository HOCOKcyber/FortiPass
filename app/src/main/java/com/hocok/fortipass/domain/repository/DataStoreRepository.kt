package com.hocok.fortipass.domain.repository

import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val password: Flow<String>
    suspend fun savePassword(newPassword: String)

    companion object{
        val MASTER_PASSWORD = stringPreferencesKey("master_password")
    }
}