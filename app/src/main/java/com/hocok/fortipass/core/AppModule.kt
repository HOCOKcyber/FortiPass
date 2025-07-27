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
import com.hocok.fortipass.domain.usecase.GetAccountsByDirectoryName
import com.hocok.fortipass.domain.usecase.GetDirectories
import com.hocok.fortipass.domain.usecase.SaveAccount
import com.hocok.fortipass.domain.usecase.SaveDirectory
import com.hocok.fortipass.domain.usecase.SearchAccounts
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
    fun provideUseCaseGet(repositoryImp: AccountRepository): GetAccounts{
        return GetAccounts(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseSave(repositoryImp: AccountRepository): SaveAccount{
        return SaveAccount(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseChangeFavoriteById(repositoryImp: AccountRepository): ChangeFavoriteById {
        return ChangeFavoriteById(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseGetAccountById(repositoryImp: AccountRepository): GetAccountById {
        return GetAccountById(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseGetDirectories(repositoryImp: AccountRepository): GetDirectories {
        return GetDirectories(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseSaveDirectory(repositoryImp: AccountRepository): SaveDirectory {
        return SaveDirectory(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideUseCaseGetAccountsByDirectoryId(repositoryImp: AccountRepository): GetAccountsByDirectoryName{
        return GetAccountsByDirectoryName(repositoryImp)
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(application: Application): DataStoreRepository {
        return DataStoreRepositoryImp(application)
    }

    @Provides
    @Singleton
    fun provideSearchAccounts(repositoryImp: AccountRepository): SearchAccounts{
        return SearchAccounts(repositoryImp)
    }

}