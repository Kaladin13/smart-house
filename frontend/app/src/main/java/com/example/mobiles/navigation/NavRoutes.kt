package com.example.mobiles.navigation

sealed class NavRoutes(val route: String) {

    object LoginScreen : NavRoutes("login")
    object RegisterScreen : NavRoutes("register")
    object HomeScreen : NavRoutes("home")
    object HomeCreationScreen : NavRoutes("home-creation")
    object ProfileScreen : NavRoutes("profile")
    object HomeListScreen: NavRoutes("home-list")
}
