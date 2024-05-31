package com.example.mobiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobiles.ui.theme.MobilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLogin = { email, password -> /* Handle login */ },
                                onNavigateToHome = { navController.navigate("home")},
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onRegister = { email, password -> /* Handle registration */ },
                                onNavigateToHome = { navController.navigate("home")},
                                onNavigateToLogin = { navController.navigate("login") }
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                onLogout = {  navController.navigate("login") },
                                onNavigateToHomeCreation = { navController.navigate("home-creation") }
                            )
                        }
                        composable("home-creation") {
                            HomeCreationScreen(
                                onNavigateToHome = { navController.navigate("home")},
                            )
                        }
                    }
                }
            }
        }
    }
}





