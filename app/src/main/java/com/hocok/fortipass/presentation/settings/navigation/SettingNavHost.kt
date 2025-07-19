package com.hocok.fortipass.presentation.settings.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hocok.fortipass.presentation.settings.importpage.ImportPage
import com.hocok.fortipass.presentation.settings.mainsetting.SettingsPage

@Composable
fun SettingNavHost(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SettingRoutes.MainSetting,
        modifier = modifier
    ) {

        composable<SettingRoutes.MainSetting> {
            SettingsPage(
                toImport = {navController.navigate(SettingRoutes.Import)}
            )
        }

        composable<SettingRoutes.Import> {
            ImportPage(
                onBack = {navController.navigateUp()}
            )
        }
    }

}