package com.hocok.fortipass.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts")
    fun getAllAccount(): Flow<List<Account>>

    @Query("SELECT * FROM directories")
    fun getAllDirectory(): Flow<List<Directory>>

    @Query("SELECT * FROM accounts WHERE isFavorite = 1")
    fun getFavoriteAccount(): Flow<List<Account>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    fun getAccountById(id: Int): Flow<Account>

    @Query("SELECT * FROM directories WHERE id = :id")
    suspend fun getDirectoryById(id: Int?): Directory

    @Query("SELECT * FROM accounts WHERE idDirectory = :idDirectory")
    suspend fun getAccountsByDirectoryId(idDirectory: Int): List<Account>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDirectory(directory: Directory)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("UPDATE accounts SET isFavorite = :changedFavorite WHERE id = :id")
    suspend fun changeFavoriteById(id: Int, changedFavorite: Boolean)
}