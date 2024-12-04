package com.example.fdsatest.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.fdsatest.ui.nav.Screens
import com.example.fdsatest.ui.theme.PrimaryColor

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    BottomAppBar(
        containerColor = PrimaryColor
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            selected = false,
            onClick = { navController.navigate(Screens.Home.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "List") },
            selected = false,
            onClick = { navController.navigate(Screens.MainScreen.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            selected = false,
            onClick = { navController.navigate(Screens.Settings.route) }
        )
    }
}