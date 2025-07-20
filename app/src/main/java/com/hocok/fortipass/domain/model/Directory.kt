package com.hocok.fortipass.domain.model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hocok.fortipass.R
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "directories"
)
data class Directory(
    @PrimaryKey val name: String = "",
)

fun Directory.getInitOrName(context: Context): String{
    return name.ifEmpty { context.getString(R.string.directory_init_name) }
}

object ExampleDirectory{
    val singleDirectory: Directory = Directory(name = "exampleDirectory")

    val listOfDirectory = listOf(
        Directory( name = "exampleDirectory"),
        Directory( name = "VK"),
        Directory( name = "Games"),
    )
}
