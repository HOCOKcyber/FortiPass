package com.hocok.fortipass.domain.usecase

import androidx.lifecycle.viewModelScope
import com.hocok.fortipass.domain.model.Account
import kotlinx.coroutines.launch

/*
* TODO("Решить  вопрос с литераломи")
*  */
class ValidAccountData {
    operator fun invoke(accountData: Account): Valid{
        if (accountData.title.isEmpty()) return Valid.TitleError("Название не должно быть пустым")
        if (accountData.login.isEmpty()) return Valid.LoginError("Логин не должен быть пустым")
        if (accountData.password.isEmpty()) return Valid.PasswordError("Пароль не должен быть пустым")
        if (accountData.siteLink.isEmpty()) return Valid.SiteLinkError("Ссылка не должна быть пустой")
        return Valid.Success("Сохранено")
    }
}

sealed class Valid(val message: String){
    class TitleError(message: String): Valid(message)
    class LoginError(message: String): Valid(message)
    class PasswordError(message: String): Valid(message)
    class SiteLinkError(message: String): Valid(message)
    class Success(message: String): Valid(message)
}