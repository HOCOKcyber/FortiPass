package com.hocok.fortipass.presentation.settings.navigation

import kotlinx.serialization.Serializable

sealed class SettingRoutes {
    @Serializable
    data object MainSetting

    @Serializable
    data object Import
}