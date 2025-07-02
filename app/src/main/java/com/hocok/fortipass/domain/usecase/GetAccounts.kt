package com.hocok.fortipass.domain.usecase

import androidx.room.Insert
import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetAccounts @Inject constructor(
    private val repository: AccountRepository
) {

    operator fun invoke(
        isFavorite: Boolean = false
    ): Flow<List<Account>> {
        return  if (isFavorite) repository.getFavoriteAccount()
        else repository.getAllAccount()
    }
}