package com.hocok.fortipass.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "directories"
)
data class Directory(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String = "",
)

object ExampleDirectory{
    val singleDirectory: Directory = Directory(id = 0, name = "exampleDirectory")

    val listOfDirectory = listOf(
        Directory(id = 0, name = "exampleDirectory"),
        Directory(id = 1, name = "VK"),
        Directory(id = 2, name = "Games"),
    )
}
