package com.example.fdsatest.ui.screens

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.fdsatest.ui.theme.FDSATestTheme

@Composable
fun SettingsScreen(
    navController: NavController,
    context: Context
) {
    FDSATestTheme{
        Text("SettingScreen")
    }
}