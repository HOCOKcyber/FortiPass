package com.hocok.fortipass.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    val isFavorite: Boolean = false,
    val idDirectory: Int? = null,
)

object ExampleAccount{
    val singleAccount = Account(id = 0, title = "GitHub", login = "exampleLogin", password = "qwerty1234", siteLink = "github.com", isFavorite = true, idDirectory = null)

    val listOfAccount = listOf(
        Account(id = 0, title = "GitHub", login = "exampleLogin", password = "qwerty1234", siteLink = "github.com", isFavorite = true, idDirectory = null),
        Account(id = 1, title = "Yandex", login = "yandexLogin", password = "yandexPassword", siteLink = "yandex.com", isFavorite = false, idDirectory = null),
        Account(id = 2, title = "Google", login = "googleLogin", password = "googlePassword", siteLink = "google.com", isFavorite = true, idDirectory = null),
    )
}
