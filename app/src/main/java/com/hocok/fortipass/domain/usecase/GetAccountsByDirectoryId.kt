package com.hocok.fortipass.domain.usecase

import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountsByDirectoryName @Inject constructor(
    val repository: AccountRepository
){
    suspend operator fun invoke(nameDirectory: String): List<Account>{
        return repository.getAccountsByDirectoryName(nameDirectory)
    }
}