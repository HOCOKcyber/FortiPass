package com.hocok.fortipass.domain.repository

import android.net.Uri
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    val hashPassword: Flow<Int?>
    val salt: Flow<ByteArray>
    val uri: Flow<String>

    suspend fun saveHashPassword(hashPassword: Int)
    suspend fun saveSalt(salt: ByteArray)
    suspend fun saveUri(uri: Uri)

    companion object{
        val HASH_MASTER_PASSWORD = intPreferencesKey("hash_master_password")
        val SALT = byteArrayPreferencesKey("salt")
        val URI = stringPreferencesKey("uri")
    }
}