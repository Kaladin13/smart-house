package com.example.mobiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mobiles.model.UserViewModel
import com.example.mobiles.model.UserViewModelFactory
import com.example.mobiles.navigation.NavBarItems
import com.example.mobiles.navigation.NavRoutes
import com.example.mobiles.navigation.screens.HomeCreationScreen
import com.example.mobiles.navigation.screens.HomeListScreen
import com.example.mobiles.navigation.screens.HomeScreen
import com.example.mobiles.navigation.screens.LoginScreen
import com.example.mobiles.navigation.screens.ProfileScreen
import com.example.mobiles.navigation.screens.RegisterScreen
import com.example.mobiles.ui.theme.MobilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userViewModel: UserViewModel = viewModel(
                factory = UserViewModelFactory()
            )
            Main(userViewModel)
        }
    }

}


private val LocalUserViewModel = staticCompositionLocalOf<UserViewModel> {
    error("No UserViewModel provided")
}

@Composable
fun Main(userVm: UserViewModel) {
    MobilesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            CompositionLocalProvider(LocalUserViewModel provides userVm) {
                Column(modifier = Modifier.fillMaxSize()) {
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.LoginScreen.route,
                        modifier = Modifier.weight(1f)
                    ) {
                        composable(NavRoutes.LoginScreen.route) {
                            LoginScreen(
                                onNavigateToHomeList = { navController.navigate(NavRoutes.HomeListScreen.route) },
                                onNavigateToRegister = { navController.navigate(NavRoutes.RegisterScreen.route) },
                                userViewModel = userVm
                            )
                        }
                        composable(NavRoutes.RegisterScreen.route) {
                            RegisterScreen(
                                onNavigateToLogin = { navController.navigate(NavRoutes.LoginScreen.route) }
                            )
                        }
                        composable(NavRoutes.HomeScreen.route + "/{homeId}") { backStackEntry ->
                            val homeId = backStackEntry.arguments?.getString("homeId")?.toLong() ?: 0
                            HomeScreen(
                                homeId = homeId,
                                userViewModel = userVm,
                                onNavigateToHomeList = { navController.navigate(NavRoutes.HomeListScreen.route) },
                            )
                        }

                        composable(NavRoutes.HomeCreationScreen.route) {
                            HomeCreationScreen(
                                userViewModel = userVm,
                                onNavigateToHomeList = { navController.navigate(NavRoutes.HomeListScreen.route) },
                            )
                        }
                        composable(NavRoutes.ProfileScreen.route) {
                            ProfileScreen()
                        }
                        composable(NavRoutes.HomeListScreen.route) {
                            HomeListScreen(userViewModel = userVm, navController)
                        }
                    }
                    if (currentRoute != NavRoutes.LoginScreen.route && currentRoute != NavRoutes.RegisterScreen.route) {
                        BottomNavigationBar(navController = navController)
                    }
                }
            }
        }
    }

}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.barItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    when (navItem.route) {
                        NavRoutes.ProfileScreen.route -> {
                            navController.navigate(NavRoutes.ProfileScreen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        }

                        NavRoutes.HomeListScreen.route -> {
                            navController.navigate(NavRoutes.HomeListScreen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        }

                        NavRoutes.HomeCreationScreen.route -> {
                            navController.navigate(NavRoutes.HomeCreationScreen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        }

                        NavRoutes.LoginScreen.route -> {
                            navController.navigate(NavRoutes.LoginScreen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                },
                icon = {
                    Icon(imageVector = navItem.image, contentDescription = navItem.title)
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}
