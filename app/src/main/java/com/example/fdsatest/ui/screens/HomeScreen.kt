package com.example.fdsatest.ui.screens

import android.content.Context
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.fdsatest.ui.theme.FDSATestTheme

@Composable
fun HomeScreen(
    navController: NavController,
    context: Context
) {
    FDSATestTheme {
        Text("homeScreen")
    }
}