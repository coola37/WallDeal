package com.zeroone.wallpaperdeal.ui.screens

sealed class Screen (val route: String){
    object LoginScreen: Screen("login_screen")
    object RegisterScreen: Screen("register_screen")
    object HomeScreen: Screen("home_screen")
    object HomeCategoryScreen: Screen("home_category_screen")
}