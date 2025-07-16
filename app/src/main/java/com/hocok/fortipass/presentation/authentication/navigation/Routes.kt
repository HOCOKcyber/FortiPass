package com.hocok.fortipass.presentation.authentication.navigation


import kotlinx.serialization.Serializable

sealed class AuthRoutes {
    @Serializable
    data object Login: AuthRoutes()
    @Serializable
    data object Registration: AuthRoutes()
}