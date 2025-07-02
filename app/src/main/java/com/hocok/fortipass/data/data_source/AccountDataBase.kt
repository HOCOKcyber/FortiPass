package com.hocok.fortipass.data.data_source


import androidx.room.Database
import androidx.room.RoomDatabase
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory

@Database(
    entities = [Account::class, Directory::class],
    version = 1,
    exportSchema = false,
)
abstract class AccountDataBase: RoomDatabase() {

    abstract val accountDao: AccountDao

    companion object{
        const val NAME = "AccountsDB"
    }
}