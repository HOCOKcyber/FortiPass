package com.hocok.fortipass.domain.usecase

import com.hocok.fortipass.R
import com.hocok.fortipass.domain.model.Account

/*
* TODO("Решить  вопрос с литераломи")
*  */
class ValidAccountData {
    operator fun invoke(accountData: Account): Valid{
        if (accountData.title.isEmpty()) return Valid.TitleError(R.string.error_wrong_account_title)
        if (accountData.login.isEmpty()) return Valid.LoginError(R.string.error_wrong_account_login)
        if (accountData.password.isEmpty()) return Valid.PasswordError(R.string.error_wrong_account_password)
        if (accountData.password.length < 8) return Valid.PasswordError(R.string.password_error_lenght)
        if (accountData.siteLink.isEmpty()) return Valid.SiteLinkError(R.string.error_wrong_account_sitelink)
        return Valid.Success(R.string.saved)
    }
}

sealed class Valid(val message: Int){
    class TitleError(message: Int): Valid(message)
    class LoginError(message: Int): Valid(message)
    class PasswordError(message: Int): Valid(message)
    class SiteLinkError(message: Int): Valid(message)
    class Success(message: Int): Valid(message)
}