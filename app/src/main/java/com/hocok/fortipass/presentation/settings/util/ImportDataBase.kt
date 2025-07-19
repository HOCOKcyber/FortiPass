package com.hocok.fortipass.presentation.settings.util

import com.hocok.fortipass.domain.model.Account
import com.hocok.fortipass.domain.model.Directory
import kotlinx.serialization.Serializable


@Serializable
data class ImportDataBase(
    val accounts: List<Account> = emptyList(),
    val directory: List<Directory> = emptyList(),
    val salt: ByteArray = ByteArray(0),
)