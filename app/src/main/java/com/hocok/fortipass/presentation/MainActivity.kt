package com.hocok.fortipass.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.core.view.WindowCompat
import com.hocok.fortipass.R
import com.hocok.fortipass.presentation.navigation.FortiPassNavHost
import com.hocok.fortipass.presentation.ui.theme.FortiPassTheme
import com.hocok.fortipass.presentation.ui.theme.secondColor
import com.hocok.fortipass.presentation.ui.theme.thirdColor
import com.hocok.fortipass.presentation.util.SafeFileCallBack
import com.hocok.fortipass.presentation.util.SafeFileContract
import com.hocok.fortipass.util.FileHelper
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.LocalDateTime


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val pickLocationAndStartServiceWithCrypto = registerForActivityResult(
        SafeFileContract(),
        SafeFileCallBack(true, this)
    )
    private val pickLocationAndStartServiceWithOutCrypto = registerForActivityResult(
        SafeFileContract(),
        SafeFileCallBack(false, this)
    )

    private lateinit var exitTime: LocalDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.navigationBarColor = secondColor.toArgb()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

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
        val entryTime = LocalDateTime.now()
        val timeLeft = Duration.between(exitTime, entryTime)

        val timeoutInSecond = 60

        if (timeLeft.seconds >= timeoutInSecond) {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        exitTime = LocalDateTime.now()
    }


     fun selectFile(isCrypto: Boolean){
         if (isCrypto) pickLocationAndStartServiceWithCrypto.launch()
         else pickLocationAndStartServiceWithOutCrypto.launch()
     }

    fun openFile(uri: Uri){
        if (!FileHelper.checkFileExists(this, uri)) {
            Toast.makeText(this, getString(R.string.file_delete_or_change), Toast.LENGTH_SHORT).show()
            return
        }

        FileHelper.openInOtherApp(this, uri)
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