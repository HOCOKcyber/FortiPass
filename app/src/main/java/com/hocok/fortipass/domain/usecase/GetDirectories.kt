package com.hocok.fortipass.domain.usecase

import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDirectories @Inject constructor(
    val repository: AccountRepository
) {
    operator fun invoke(): Flow<List<Directory>> {
        return repository.getAllDirectory()
    }
}