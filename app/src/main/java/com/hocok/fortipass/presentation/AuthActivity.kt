package com.hocok.fortipass.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hocok.fortipass.domain.repository.DataStoreRepository
import com.hocok.fortipass.presentation.authentication.navigation.AuthNavigation
import com.hocok.fortipass.presentation.authentication.navigation.AuthRoutes
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity: ComponentActivity() {

    @Inject
    lateinit var dataStoreRep: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent{
            FortiPassTheme {
                val startDestination = remember{ mutableStateOf<AuthRoutes?>(null) }

                LaunchedEffect(true) {
                   val hashPassword = dataStoreRep.hashPassword.first()

                    startDestination.value = if (hashPassword == null) AuthRoutes.Registration
                                        else AuthRoutes.Login
                }

                if (startDestination.value == null){
                    Box(Modifier.fillMaxSize().background(Color.Red))
                } else {
                    AuthNavigation(
                        startDestination = startDestination.value!!,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
