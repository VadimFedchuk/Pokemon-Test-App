package com.vf.pokemontest.presentation.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    wishlistBadgeCount: Int = 0
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar {
        NavRoutes.bottomBarTabs.forEach { tab ->
            val isSelected = backStackEntry?.destination?.hierarchy?.any {
                it.route == tab.route
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navigateToTab(navController, tab)
                },
                icon = {
                    if (tab is NavRoutes.Wishlist && wishlistBadgeCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(text = wishlistBadgeCount.toString())
                                }
                            }
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = stringResource(tab.labelResId)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = stringResource(tab.labelResId)
                        )
                    }
                },
                label = { Text(text = stringResource(tab.labelResId)) },
                colors = NavigationBarItemDefaults.colors()
            )
        }
    }
}

private fun navigateToTab(navController: NavHostController, tab: NavRoutes) {
    navController.navigate(tab.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}