package com.hocok.fortipass.service.model

import android.view.autofill.AutofillId
import kotlinx.serialization.Serializable

data class ParseAccount(
    val accountId: AutofillId,
    val passwordId: AutofillId,
)