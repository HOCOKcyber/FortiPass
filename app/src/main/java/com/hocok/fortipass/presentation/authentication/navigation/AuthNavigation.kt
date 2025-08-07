package com.hocok.fortipass.presentation.authentication.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hocok.fortipass.presentation.MainActivity
import com.hocok.fortipass.presentation.authentication.login.LoginPage
import com.hocok.fortipass.presentation.authentication.registration.RegistrationPage

@Composable
fun AuthNavigation(
    startDestination: AuthRoutes,
    modifier: Modifier = Modifier,
){

    val navController = rememberNavController()
    val context = LocalContext.current
    Scaffold(
        modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(it)
        ) {

            composable<AuthRoutes.Login>{
                LoginPage(
                    onContinue = { makeIntentToHomePage(context) }
                )
            }

            composable<AuthRoutes.Registration>{
                RegistrationPage(
                    onContinue = { makeIntentToHomePage(context) }
                )
            }
        }
    }
}

private fun makeIntentToHomePage(context: Context){
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}