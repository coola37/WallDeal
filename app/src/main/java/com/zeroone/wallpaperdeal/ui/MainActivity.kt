package com.zeroone.wallpaperdeal.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.screens.login.LoginScreen
import com.zeroone.wallpaperdeal.ui.screens.register.RegisterScreen
import com.zeroone.wallpaperdeal.ui.theme.WallpaperDealTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WallpaperDealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.LoginScreen.route ){
                        composable(Screen.LoginScreen.route){
                            LoginScreen(navController = navController)
                        }
                        composable(Screen.RegisterScreen.route){
                            RegisterScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
