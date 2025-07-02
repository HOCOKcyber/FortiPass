package com.hocok.fortipass.domain.usecase

import com.hocok.fortipass.domain.model.Directory
import com.hocok.fortipass.domain.repository.AccountRepository
import javax.inject.Inject

class SaveDirectory @Inject constructor(
    val repository: AccountRepository
) {
    suspend operator fun invoke(directory: Directory){
        repository.saveDirectory(directory)
    }
}