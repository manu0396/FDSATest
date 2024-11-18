package com.example.fdsatest.ui.nav

sealed class Screens (val route: String) {

    object MainScreen : Screens("main_screen")

    object Auth : Screens("auth")

    object DetailScreen: Screens("detail_screen")
}