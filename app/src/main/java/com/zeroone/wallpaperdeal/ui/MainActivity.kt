package com.zeroone.wallpaperdeal.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
import com.zeroone.wallpaperdeal.ui.screens.ScreenCallback
import com.zeroone.wallpaperdeal.ui.screens.home.HomeCategoryScreen
import com.zeroone.wallpaperdeal.ui.screens.home.HomeScreen
import com.zeroone.wallpaperdeal.ui.screens.home.SelectedCategoryScreen
import com.zeroone.wallpaperdeal.ui.screens.profile.EditProfileScreen
import com.zeroone.wallpaperdeal.ui.screens.profile.OtherProfileScreen
import com.zeroone.wallpaperdeal.ui.screens.profile.SettingsScreen
import com.zeroone.wallpaperdeal.ui.screens.requests.RequestsScreen
import com.zeroone.wallpaperdeal.ui.screens.search.SearchScreen
import com.zeroone.wallpaperdeal.ui.screens.share.PushWallpaperScreen
import com.zeroone.wallpaperdeal.ui.screens.share.ShareScreen
import com.zeroone.wallpaperdeal.ui.screens.walldeal.WallDealScreen
import com.zeroone.wallpaperdeal.ui.screens.wallpaperview.WallpaperViewScreen
import com.zeroone.wallpaperdeal.ui.theme.WallpaperDealTheme
import com.zeroone.wallpaperdeal.utils.NetworkConnection
import com.zeroone.wallpaperdeal.utils.setWallpaper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity(), ScreenCallback {
    companion object {
        const val REQUEST_SET_WALLPAPER_PERMISSION = 1000
    }
    @Inject
    lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    @Inject
    lateinit var storage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupAuthListener()
        //auth.signOut()
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
                            SelectedCategoryScreen(navController = navController, auth = auth)
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
                            WallpaperViewScreen(navController = navController, callback = this@MainActivity, auth = auth)
                        }
                        composable(Screen.SearchScreen.route){
                            SearchScreen(navController = navController, auth = auth)
                        }
                        composable("${Screen.OtherProfileScreen.route}/{userId}",
                            arguments = listOf(
                                navArgument("userId"){
                                    type = NavType.StringType
                                    defaultValue = ""
                                    nullable = true
                                }
                            )
                        ){
                            OtherProfileScreen(auth = auth, navController = navController )
                        }
                        composable(Screen.WallDealScreen.route){
                            WallDealScreen(auth = auth, navController = navController, storage = storage)
                        }
                        composable(Screen.RequestsScreen.route){
                            RequestsScreen(auth = auth, navController = navController)
                        }
                        composable("${Screen.EditProfileScreen.route}/{userId}",
                            arguments = listOf(
                                navArgument("userId"){
                                    type = NavType.StringType
                                    defaultValue = ""
                                    nullable = true
                                }
                            )
                        ){
                            EditProfileScreen(navController = navController, storage = storage )
                        }
                        composable(Screen.SettingsScreen.route){
                            SettingsScreen(auth = auth, navController = navController)
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

    override fun onSetWallpaperClick(bitmap: Bitmap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SET_WALLPAPER) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SET_WALLPAPER),
                REQUEST_SET_WALLPAPER_PERMISSION
            )
        } else {
            setWallpaper(this, bitmap)
        }
    }

    @Composable
    private fun NetworkCheck(){
        val networkConnection = NetworkConnection(applicationContext)
        LaunchedEffect(Dispatchers.IO){
            networkConnection.observe(this@MainActivity, Observer{ isConnected->
                if(isConnected){
                    Log.d("Network Check", "OK")
                }
                else{

                }
            })
        }
    }
}
