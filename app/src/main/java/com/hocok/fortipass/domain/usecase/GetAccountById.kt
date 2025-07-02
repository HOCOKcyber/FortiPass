package com.hocok.fortipass.domain.usecase

import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountById @Inject constructor(
    val repository: AccountRepository
) {
    operator fun invoke(id: Int): Flow<Account> {
        return repository.getAccountById(id)
    }
}