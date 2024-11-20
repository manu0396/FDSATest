package com.example.fdsatest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.fdsatest.ui.nav.Screens
import com.example.fdsatest.ui.screens.DestinationScreen
import com.example.fdsatest.ui.screens.DetailDestinationScreen
import com.example.fdsatest.ui.theme.FDSATestTheme
import com.example.fdsatest.ui.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FDSATestTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screens.Auth.route){
                    navigation(
                        startDestination = Screens.MainScreen.route,
                        route = Screens.Auth.route
                    ){
                        composable(Screens.MainScreen.route){ navBackStackEntry ->
                            DestinationScreen(
                                context = applicationContext,
                                viewModel = navBackStackEntry.sharedViewModel(navController),
                                navController = navController
                            )
                        }
                        composable(Screens.DetailScreen.route) { navBackStackEntry ->
                            DetailDestinationScreen(
                                context = applicationContext,
                                viewModel = navBackStackEntry.sharedViewModel(navController),
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Function to instantiate viewmodel, passing route in case it exists.
 */
@Composable
fun NavBackStackEntry.sharedViewModel(navController: NavController): SharedViewModel {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}
