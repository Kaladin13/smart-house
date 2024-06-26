package com.example.mobiles.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person

object NavBarItems {

    val barItems = listOf(
        BarItem(
            title = "Profile",
            image = Icons.Filled.Person,
            route = "profile"
        ),
        BarItem(
            title = "My houses",
            image = Icons.Filled.Home,
            route = "home-list"
        ),
        BarItem(
            title = "Create house",
            image = Icons.Filled.Add,
            route = "home-creation"
        ),
        BarItem(
            title = "Logout",
            image = Icons.Filled.ExitToApp,
            route = "login"
        ),

    )

}