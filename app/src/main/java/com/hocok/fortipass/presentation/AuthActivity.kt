package com.hocok.fortipass.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.hocok.fortipass.domain.repository.DataStoreRepository
import com.hocok.fortipass.presentation.authentication.navigation.AuthNavigation
import com.hocok.fortipass.presentation.authentication.navigation.AuthRoutes
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
                   val password = dataStoreRep.password.first()

                    startDestination.value = if (password.isEmpty()) AuthRoutes.Registration
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
