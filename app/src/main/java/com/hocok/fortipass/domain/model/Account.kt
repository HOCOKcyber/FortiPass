package com.hocok.fortipass.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hocok.fortipass.domain.util.cipher.CipherManager
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "accounts",
//    foreignKeys = [
//        ForeignKey(
//            entity = Directory::class,
//            parentColumns = ["idDirectory"],
//            childColumns = ["id"],
//            onDelete = ForeignKey.NO_ACTION,
//            onUpdate = ForeignKey.NO_ACTION
//        )
//    ]
)
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String = "",
    val login: String = "",
    val password: String = "",
    val siteLink: String = "",
    val iv: String = "",
    val isFavorite: Boolean = false,
    val idDirectory: Int? = 0,
)

fun Account.getDecodeAccount(): Account{
    return copy(password = CipherManager.decrypt(password, iv))
}

fun Account.getEncodeAccount(): Account{
    val (encryptPassword, iv) = CipherManager.encrypt(password)
    return copy(password = encryptPassword, iv = iv)
}

fun Account.toNotCryptoExport(): Account{
    return copy(
        password = CipherManager.decrypt(password, iv),
        iv = ""
    )
}

object ExampleAccount{
    val singleAccount = Account(id = 0, title = "GitHub", login = "exampleLogin", password = "qwerty1234", siteLink = "github.com", isFavorite = true, idDirectory = 0)

    val listOfAccount = listOf(
        Account(id = 0, title = "GitHub", login = "exampleLogin", password = "qwerty1234", siteLink = "github.com", isFavorite = true, idDirectory = 0),
        Account(id = 1, title = "Yandex", login = "yandexLogin", password = "yandexPassword", siteLink = "yandex.com", isFavorite = false, idDirectory = 1),
        Account(id = 2, title = "Google", login = "googleLogin", password = "googlePassword", siteLink = "google.com", isFavorite = true, idDirectory = 2),
    )
}
