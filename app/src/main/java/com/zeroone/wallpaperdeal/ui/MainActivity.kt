package com.zeroone.wallpaperdeal.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.ui.screens.profile.ProfileScreen
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.screens.home.HomeCategoryScreen
import com.zeroone.wallpaperdeal.ui.screens.home.HomeScreen
import com.zeroone.wallpaperdeal.ui.screens.home.SelectedCategoryScreen
import com.zeroone.wallpaperdeal.ui.screens.share.PushWallpaperScreen
import com.zeroone.wallpaperdeal.ui.screens.share.ShareScreen
import com.zeroone.wallpaperdeal.ui.screens.wallpaperview.WallpaperViewScreen
import com.zeroone.wallpaperdeal.ui.theme.WallpaperDealTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    @Inject
    lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAuthListener()
        setContent {
            WallpaperDealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.HomeScreen.route ){
                        composable(Screen.HomeScreen.route){
                            HomeScreen(navController = navController, auth = auth)
                        }
                        composable(Screen.HomeCategoryScreen.route){
                            HomeCategoryScreen(navController = navController)
                        }
                        composable(Screen.ShareScreen.route){
                            ShareScreen(navController = navController, storage = storage)
                        }
                        composable("${Screen.PushWallpaperScreen.route}/{wallpaperId}",
                            arguments = listOf(
                                navArgument("wallpaperId") {
                                    type = NavType.StringType
                                    defaultValue = ""
                                    nullable = true
                                }
                            )
                        ) {
                            PushWallpaperScreen(navController = navController, storage = storage, auth = auth)
                        }
                        composable(Screen.ProfileScreen.route){
                            ProfileScreen(navController = navController, auth = auth)
                        }
                        composable("${ Screen.SelectedCategoryScreen.route}/{category}",
                            arguments = listOf(
                                navArgument("category"){
                                    type = NavType.StringType
                                    defaultValue = ""
                                    nullable = true
                                }
                            )
                            ){
                            SelectedCategoryScreen(navController = navController)
                        }
                        composable("${ Screen.WallpaperViewScreen.route }/{wallpaperId}",
                            arguments = listOf(
                                navArgument("wallpaperId"){
                                    type = NavType.StringType
                                    defaultValue = ""
                                    nullable = true
                                }
                            )
                        ){
                            WallpaperViewScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)

    }

    override fun onStop() {
        super.onStop()
        authListener?.let {
            auth.removeAuthStateListener(authListener)
        }
    }
    private fun setupAuthListener(){
        lifecycleScope.launch {
            authListener = object : FirebaseAuth.AuthStateListener{
                override fun onAuthStateChanged(p0: FirebaseAuth) {
                    val user: FirebaseUser? = auth.currentUser
                    user?.let {
                        return
                    }?: kotlin.run {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
