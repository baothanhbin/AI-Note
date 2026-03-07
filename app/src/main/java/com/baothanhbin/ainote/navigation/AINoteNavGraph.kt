package com.baothanhbin.ainote.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavScreen(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Notes : BottomNavScreen("home_route", "Notes", Icons.Default.Home)
    data object Tags : BottomNavScreen("tags_route", "Tags", Icons.Default.Label)
    data object Graph : BottomNavScreen("graph_route", "Graph", Icons.Default.AccountTree)
    data object Settings : BottomNavScreen("settings_route", "Settings", Icons.Default.Settings)
}

val bottomNavScreens = listOf(
    BottomNavScreen.Notes,
    BottomNavScreen.Tags,
    BottomNavScreen.Graph,
    BottomNavScreen.Settings
)

@Composable
fun AINoteBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = bottomNavScreens.any { it.route == currentDestination?.route }

    if (showBottomBar) {
        NavigationBar(modifier = modifier) {
            bottomNavScreens.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title
                        )
                    },
                    label = { Text(screen.title) },
                    selected = currentDestination?.isBottomNavDestination(screen) == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = androidx.compose.material3.MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            }
        }
    }
}

private fun NavDestination?.isBottomNavDestination(screen: BottomNavScreen): Boolean {
    return this?.hierarchy?.any { it.route == screen.route } == true
}
