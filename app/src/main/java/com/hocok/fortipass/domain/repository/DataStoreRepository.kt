package com.hocok.fortipass.domain.repository

import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val hashPassword: Flow<Int?>
    val salt: Flow<ByteArray>


    suspend fun saveHashPassword(hashPassword: Int)
    suspend fun saveSalt(salt: ByteArray)

    companion object{
        val HASH_MASTER_PASSWORD = intPreferencesKey("hash_master_password")
        val SALT = byteArrayPreferencesKey("salt")
    }
}