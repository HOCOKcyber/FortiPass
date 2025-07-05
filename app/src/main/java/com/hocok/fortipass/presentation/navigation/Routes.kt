package com.hocok.fortipass.presentation.navigation

import androidx.annotation.DrawableRes
import com.hocok.fortipass.R
import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Home: Routes()
    @Serializable
    data class Generator(val isFromAddEdit: Boolean = false): Routes()
    @Serializable
    data object Setting: Routes()
    @Serializable
    data class DetailsAccount(val id: Int): Routes()
    @Serializable
    data class DetailsDirectory(val idDirectory: Int): Routes()
    @Serializable
    data class AddEditAccount(val id: Int?): Routes()
    @Serializable
    data class AddEditDirectory(val id: Int?): Routes()
}

data class BottomRoutes(
    @DrawableRes val iconRes: Int,
    val route: Routes,
)

val bottomRoutesList = listOf(
    BottomRoutes(iconRes = R.drawable.storage, route = Routes.Home),
    BottomRoutes(iconRes = R.drawable.lock, route = Routes.Generator()),
    BottomRoutes(iconRes = R.drawable.setting, route = Routes.Setting),
)