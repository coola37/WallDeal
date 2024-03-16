package com.zeroone.wallpaperdeal.ui.screens

sealed class Screen (val route: String){
    object LoginScreen: Screen("login_screen")
    object RegisterScreen: Screen("register_screen")
    object HomeScreen: Screen("home_screen")
    object HomeCategoryScreen: Screen("home_category_screen")
    object ShareScreen: Screen ("share_screen")
    object PushWallpaperScreen: Screen("push_wallpaper_screen")
    object ProfileScreen: Screen("profile_screen")
    object SelectedCategoryScreen: Screen("selected_category_screen")
    object WallpaperViewScreen: Screen("wallpaper_view_screen")
    object SearchScreen: Screen("search_screen")
}