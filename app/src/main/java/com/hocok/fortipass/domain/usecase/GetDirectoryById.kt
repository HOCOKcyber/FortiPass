package com.hocok.fortipass.domain.usecase

import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.repository.AccountRepository
import javax.inject.Inject

class GetDirectoryById @Inject constructor(
    val repository: AccountRepository
) {
    suspend operator fun invoke(id: Int?): Directory{
        return repository.getDirectoryById(id)
    }
}