package com.hocok.fortipass.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import com.hocok.fortipass.presentation.navigation.FortiPassNavHost
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.secondColor
import com.hocok.fortipass.presentation.ui.theme.thirdColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.navigationBarColor = secondColor.toArgb()
        setContent {
            FortiPassTheme {
                FortiPassNavHost(
                    Modifier.fillMaxSize()
                )
                StatusBarProtection()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }
}


@Composable
private fun StatusBarProtection(
    heightProvider: () -> Float = calculateStatusBarHeight(),
) {
    Canvas(Modifier.fillMaxSize()) {
        val calculatedHeight = heightProvider()
        drawRect(
            color = thirdColor,
            size = Size(size.width, calculatedHeight),
        )
    }
}

@Composable
fun calculateStatusBarHeight(): () -> Float {
    val statusBars = WindowInsets.statusBars
    val density = LocalDensity.current
    return { statusBars.getTop(density).times(1.2f) }
}