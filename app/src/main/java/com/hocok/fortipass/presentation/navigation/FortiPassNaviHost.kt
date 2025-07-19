package com.hocok.fortipass.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hocok.fortipass.presentation.account.addedit.AddEditAccountPage
import com.hocok.fortipass.presentation.account.details.DetailsAccountPage
import com.hocok.fortipass.presentation.directory.addedit.AddEditDirectoryPage
import com.hocok.fortipass.presentation.directory.details.DetailsDirectoryPage
import com.hocok.fortipass.presentation.generator.GeneratorPage
import com.hocok.fortipass.presentation.homepage.HomePage
import com.hocok.fortipass.presentation.settings.mainsetting.SettingsPage
import com.hocok.fortipass.presentation.settings.navigation.SettingNavHost
import com.hocok.fortipass.presentation.ui.TopBarTitles
import com.hocok.fortipass.presentation.ui.theme.onSecondColor
import com.hocok.fortipass.presentation.ui.theme.secondColor
import com.hocok.fortipass.presentation.ui.theme.selectedItemColor
import com.hocok.fortipass.util.PASSWORD_KEY


@Composable
fun FortiPassNavHost(
    modifier: Modifier = Modifier
){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            )
        },
        modifier = modifier
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
            modifier = Modifier.padding(contentPadding)
        ) {
            composable<Routes.Home>{
                HomePage(
                    toDetailsAccount = {id -> navController.navigate(Routes.DetailsAccount(id))},
                    toAddAccount = {navController.navigate(Routes.AddEditAccount(null))},
                    toAddDirectory = {navController.navigate(Routes.AddEditDirectory(null))},
                    toDetailsDirectory = {id -> navController.navigate(Routes.DetailsDirectory(id))},
                )
            }

            composable<Routes.Generator> {backStack ->
                val isFromAddEdit = backStack.toRoute<Routes.Generator>().isFromAddEdit

                val savePasswordFromGenerator: (String) -> Unit = {newPassword ->
                        navController
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(PASSWORD_KEY, newPassword)
                    Log.d("Set from generator", newPassword)
                        navController.popBackStack()
                }

                GeneratorPage(
                    onBack = {navController.navigateUp()},
                    modifier = Modifier.padding(contentPadding),
                    isFromAddEdit = isFromAddEdit,
                    savePasswordFromGenerator = savePasswordFromGenerator,
                )
            }
            composable<Routes.Setting> {
                Text(
                    text = stringResource(TopBarTitles.SETTING.strId),
                )
            }

            composable<Routes.AddEditAccount>{
                val backEntryId = it.toRoute<Routes.AddEditAccount>().id

                val password = it.savedStateHandle.get<String>(PASSWORD_KEY) ?: ""

                val title = stringResource(
                    if (backEntryId == null) TopBarTitles.ADD.strId
                    else TopBarTitles.EDIT.strId
                )


                AddEditAccountPage(
                    title = title,
                    onBack = {navController.navigateUp()},
                    password = password,
                    toGenerator = {navController.navigate(Routes.Generator(isFromAddEdit = true))},
                )
            }

            composable<Routes.DetailsAccount> {
                val accountId = it.toRoute<Routes.DetailsAccount>().id
                DetailsAccountPage(
                    onBack = {navController.navigateUp()},
                    onEdit = {navController.navigate(Routes.AddEditAccount(accountId))}
                )
            }

            composable<Routes.AddEditDirectory> {
                AddEditDirectoryPage(
                    title =  stringResource( TopBarTitles.ADD.strId),
                    onBack = {navController.navigateUp()}
                )
            }

            composable<Routes.DetailsDirectory> {
                DetailsDirectoryPage(
                    toDetailsAccount = {navController.navigate(Routes.DetailsAccount(it))},
                    onBack = {navController.navigateUp()}
                )
            }

            composable<Routes.Setting> {
                SettingNavHost()
            }

        }

    }
}

@Composable
private fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
){

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = secondColor,
        contentColor = onSecondColor,
        modifier = modifier
    ){
        bottomRoutesList.forEach { bottomRoute ->

            val isSelected = currentDestination?.hierarchy?.any{
                it.hasRoute(bottomRoute.route::class)
            } == true

            val offset = if (isSelected) Offset(0f, -3f)
            else Offset(0f, 3f)

            BottomNavigationItem(
                selected = isSelected,
                onClick = { if (!isSelected) navController.navigate(bottomRoute.route) },
                icon = {
                    Icon(
                        painter = painterResource(bottomRoute.iconRes),
                        contentDescription = null,
                        tint = if (isSelected) selectedItemColor else onSecondColor,
                        modifier = Modifier.size(30.dp)
                    )
                       },
                modifier = Modifier.offset(offset.x.dp, offset.y.dp)
            )
        }
    }
}