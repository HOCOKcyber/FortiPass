package com.hocok.fortipass.presentation

import android.os.Bundle
import android.widget.GridLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hocok.fortipass.domain.repository.DataStoreRepository
import com.hocok.fortipass.presentation.authentication.navigation.AuthNavigation
import com.hocok.fortipass.presentation.authentication.navigation.AuthRoutes
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.backgroundColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import com.hocok.fortipass.R

@AndroidEntryPoint
class AuthActivity: ComponentActivity() {

    @Inject
    lateinit var dataStoreRep: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        window.navigationBarColor = backgroundColor.toArgb()
        setContent{
            FortiPassTheme {
                val startDestination = remember{ mutableStateOf<AuthRoutes?>(null) }

                LaunchedEffect(true) {
                   val hashPassword = dataStoreRep.hashPassword.first()

                    delay(1000)
                    startDestination.value = if (hashPassword == null) AuthRoutes.Registration
                                        else AuthRoutes.Login
                }

                if (startDestination.value == null){
                    AppPreview()
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

@Preview
@Composable
fun AppPreview(){
    Box(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.align(Alignment.Center)
                .size(200.dp)
        )
    }
}
