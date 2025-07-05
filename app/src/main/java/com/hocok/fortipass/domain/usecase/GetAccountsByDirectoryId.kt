package com.hocok.fortipass.domain.usecase

import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountsByDirectoryId @Inject constructor(
    val repository: AccountRepository
){
    suspend operator fun invoke(idDirectory: Int): List<Account>{
        return repository.getAccountsByDirectoryId(idDirectory)
    }
}