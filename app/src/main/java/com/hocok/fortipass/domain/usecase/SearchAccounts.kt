package com.hocok.fortipass.domain.usecase

import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.repository.AccountRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class SearchAccounts @Inject constructor(
    private val repository: AccountRepository,
) {
    operator fun invoke(request: String, directoryName: String?): Flow<List<Account>> {
        return repository.searchAccountByRequest(request, directoryName ?: "")
    }
}