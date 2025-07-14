package com.hocok.fortipass.domain.usecase

import androidx.annotation.StringRes
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.authentication.registration.RegistrationEvent
import kotlinx.serialization.descriptors.PrimitiveKind

class RegistrationPasswordValidation {

    operator fun invoke(password: String, repeatedPassword: String): PasswordValidation{

        return  if (password != repeatedPassword) PasswordValidation(isSuccess = false, errorMessage = R.string.password_errro_diffrence)
                else if (password.length <= 8) PasswordValidation(isSuccess = false, errorMessage = R.string.password_error_lenght)
                else PasswordValidation(isSuccess = true)
    }
}


data class PasswordValidation(
    val isSuccess: Boolean = false,
    @StringRes val errorMessage: Int? = null
)
