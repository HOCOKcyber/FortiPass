package com.hocok.fortipass.data.repository

import com.hocok.fortipass.data.data_source.AccountDao
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepositoryImp @Inject constructor(
    private val dao: AccountDao
): AccountRepository {

    override fun getAllAccount(): Flow<List<Account>> {
        return dao.getAllAccount()
    }

    override fun getAllDirectory(): Flow<List<Directory>> {
        return dao.getAllDirectory()
    }

    override fun getFavoriteAccount(): Flow<List<Account>> {
        return dao.getFavoriteAccount()
    }

    override fun getAccountById(id: Int): Flow<Account> {
        return dao.getAccountById(id)
    }

    override suspend fun getAccountsByDirectoryName(nameDirectory: String): List<Account> {
        return dao.getAccountsByDirectoryName(nameDirectory)
    }

    override suspend fun insertAccount(account: Account) {
        dao.insertAccount(account)
    }

    override suspend fun saveDirectory(directory: Directory) {
        dao.insertDirectory(directory)
    }

    override suspend fun deleteAccount(account: Account) {
        dao.deleteAccount(account)
    }

    override suspend fun changeFavoriteById(id: Int, changedFavorite: Boolean) {
        dao.changeFavoriteById(id, changedFavorite)
    }
}