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
        /*private var instance: AccountDataBase? = null

        fun getInstance(
            context: Context,
            scope: CoroutineScope,
        ) = instance ?:
            Room.databaseBuilder(
                context = context,
                klass = AccountDataBase::class.java,
                name = NAME
            )
            .addCallback(DirectoryCallBack(scope))
            .build()*/
    }

    /*private class DirectoryCallBack(
        private val scope: CoroutineScope
    ): RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            instance?.let {
                scope.launch {
                    it.accountDao.insertDirectory(Directory())
                }
            }
        }
    }*/
}