package com.example.fdsatest.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.fdsatest.R
import com.example.fdsatest.ui.animations.TripleOrbitLoadingAnimation
import com.example.fdsatest.ui.components.AppBar
import com.example.fdsatest.ui.components.BottomNavigationBar
import com.example.fdsatest.ui.components.H1Text
import com.example.fdsatest.ui.theme.FDSATestTheme
import com.example.fdsatest.ui.viewmodel.SharedViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: SharedViewModel,
    context: Context
) {

    val showLoading by viewModel.showLoading.collectAsState()
    FDSATestTheme {
        Scaffold(
            topBar = {
                AppBar(
                    title = context.getString(R.string.main_title),
                    onBackClick = { navController.popBackStack() }
                )
            },
            bottomBar = { BottomNavigationBar(navController) },
            contentWindowInsets = WindowInsets(16.dp)
        ) { paddingValues ->
            if (showLoading) {
                TripleOrbitLoadingAnimation()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                H1Text(
                    text = "homeScreen",
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}