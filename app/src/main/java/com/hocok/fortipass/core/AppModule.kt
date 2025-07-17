package com.hocok.fortipass.core

import android.app.Application
import androidx.room.Room
import com.hocok.fortipass.data.data_source.AccountDao
import com.hocok.fortipass.data.data_source.AccountDataBase
import com.hocok.fortipass.data.repository.AccountRepositoryImp
import com.hocok.fortipass.data.repository.DataStoreRepositoryImp
import com.hocok.fortipass.domain.repository.AccountRepository
import com.hocok.fortipass.domain.repository.DataStoreRepository
import com.hocok.fortipass.domain.usecase.ChangeFavoriteById
import com.hocok.fortipass.domain.usecase.GetAccountById
import com.hocok.fortipass.domain.usecase.GetAccounts
import com.hocok.fortipass.domain.usecase.GetAccountsByDirectoryId
import com.hocok.fortipass.domain.usecase.GetDirectories
import com.hocok.fortipass.domain.usecase.GetDirectoryById
import com.hocok.fortipass.domain.usecase.SaveAccount
import com.hocok.fortipass.domain.usecase.SaveDirectory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataBase(application: Application): AccountDataBase{
        return Room.databaseBuilder(
            context = application,
            klass = AccountDataBase::class.java,
            name = AccountDataBase.NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(db: AccountDataBase): AccountDao {
        return db.accountDao
    }

    @Provides
    @Singleton
    fun provideDataBaseRepository(dao: AccountDao): AccountRepository {
        return AccountRepositoryImp(dao)
    }

    @Provides
    @Singleton
    fun provideUseCaseGet(repositoryImp: AccountRepositoryImp): GetAccounts{
        return GetAccounts(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseSave(repositoryImp: AccountRepositoryImp): SaveAccount{
        return SaveAccount(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseChangeFavoriteById(repositoryImp: AccountRepositoryImp): ChangeFavoriteById {
        return ChangeFavoriteById(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseGetAccountById(repositoryImp: AccountRepositoryImp): GetAccountById {
        return GetAccountById(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseGetDirectories(repositoryImp: AccountRepositoryImp): GetDirectories {
        return GetDirectories(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseSaveDirectory(repositoryImp: AccountRepositoryImp): SaveDirectory {
        return SaveDirectory(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseGetDirectoryById(repositoryImp: AccountRepositoryImp): GetDirectoryById{
        return GetDirectoryById(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseGetAccountsByDirectoryId(repositoryImp: AccountRepositoryImp): GetAccountsByDirectoryId{
        return GetAccountsByDirectoryId(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(application: Application): DataStoreRepository {
        return DataStoreRepositoryImp(application)
    }

}