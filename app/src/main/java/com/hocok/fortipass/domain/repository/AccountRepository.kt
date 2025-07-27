package com.hocok.fortipass.domain.repository

import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    fun getAllAccount(): Flow<List<Account>>

    fun getAllDirectory(): Flow<List<Directory>>

    fun getFavoriteAccount(): Flow<List<Account>>

    fun getAccountById(id: Int): Flow<Account>

    fun searchAccountByRequest(request: String, directoryName: String) : Flow<List<Account>>

    suspend fun getAccountsByDirectoryName(nameDirectory: String): List<Account>

    suspend fun insertAccount(account: Account)
    suspend fun saveDirectory(directory: Directory)

    suspend fun deleteAccount(account: Account)

    suspend fun changeFavoriteById(id: Int, changedFavorite: Boolean)
}