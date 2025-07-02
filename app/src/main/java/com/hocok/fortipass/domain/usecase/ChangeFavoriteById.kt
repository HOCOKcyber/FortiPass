package com.hocok.fortipass.domain.usecase

import com.hocok.fortipass.domain.repository.AccountRepository
import javax.inject.Inject

class ChangeFavoriteById @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(id: Int, changedFavorite: Boolean){
        repository.changeFavoriteById(id, changedFavorite)
    }
}