package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.zeroone.wallpaperdeal.data.local.WallpaperDatabase
import com.zeroone.wallpaperdeal.data.model.LikeRequest
import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.ui.screens.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WallpaperViewViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val userRepository: UserRepository,
    private val wallpaperDatabase: WallpaperDatabase,
    @ApplicationContext context: Context
): ViewModel(){

    val wallpaperState : MutableState<Wallpaper?> = mutableStateOf(null)
    val checkLikeState : MutableState<Boolean> = mutableStateOf(false)
    val checkFavoritesState : MutableState<Boolean> = mutableStateOf(false)
    private var job: Job? = null

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val token = sharedPreferences.getString("firebase_token", null)

    fun fetchWallpaper(wallpaperId: Int){
        job?.cancel()
        Log.e("Wallpaper Fetched", "true")
        job = getWallpaper(wallpaperId = wallpaperId).onEach {
            wallpaperState.value = it
        }.launchIn(viewModelScope)
    }
    private fun getWallpaper(wallpaperId: Int): Flow<Wallpaper> = flow{
        try {
            token?.let {
                val wallpaper = wallpaperRepository.getWallpaperById(wallpaperId = wallpaperId, token = it)
                emit(wallpaper)
            }
        }catch (e: IOException){
            throw e
        }
    }

    fun likeOrDislike(wallpaperId: Int, likeRequest: LikeRequest){
        try {
            token?.let {
                viewModelScope.launch {
                    wallpaperRepository.likeOrDislike(wallpaperId = wallpaperId, likeRequest = likeRequest, token = it)
                    Log.e("likeOrDislike:", "succes")
                    checkLikeState.value = !checkLikeState.value
                    fetchWallpaper(wallpaperId = wallpaperId)
                }
            }
        }catch (ex: IOException){
            Log.e("likeOrDislike Error:", ex.message.toString())
        }
    }

    fun checkLike(wallpaperId: Int, userId: String){
        try {
            token?.let {
                viewModelScope.launch{
                    checkLikeState.value = wallpaperRepository.checkLike(wallpaperId = wallpaperId, currentUserId = userId, token = it)
                    Log.e("checkLike ViewModel:", checkLikeState.value.toString())
                }
            }
        }catch (ex: IOException){
            Log.e("CheckLike Error:", ex.message.toString())
        }
    }

    fun addOrRemoveFavorites(wallpaperId: Int, userId: String){
        try {
            token?.let {
                viewModelScope.launch {
                    wallpaperRepository.addFavorite(wallpaperId = wallpaperId, userId = userId, token = it)
                    checkFavoritesState.value = !checkFavoritesState.value
                }
            }
        }catch (ex: IOException){
            Log.e("addOrRemoveFavorites error:", ex.message.toString())
        }
    }

    fun checkFavorites(userId: String, wallpaperId: Int){
        try {
            token?.let {
                viewModelScope.launch {
                    checkFavoritesState.value = userRepository.checkFavorites(userId = userId, wallpaperId = wallpaperId, token = it)
                    Log.e("checkFavorites in viewmodel state", userRepository.checkFavorites(userId = userId,
                        wallpaperId = wallpaperId, token = it).toString())
                }
            }

        }catch (e: RuntimeException){
            Log.e("checkFavorites in viewmodel", e.message.toString())
            throw e
        }
    }

    fun removeWallpaper(wallpaperId: Int, navController: NavController){
        try{
            token?.let {
                viewModelScope.launch {
                    wallpaperRepository.removeWallpaper(wallpaperId = wallpaperId, token = it)
                    val wallpaper = wallpaperRepository.getWallpaperById(wallpaperId = wallpaperId, token = it)
                    wallpaperDatabase.wallpaperDao().deleteWallpaper(wallpaper)
                }
            }
        }catch (e: RuntimeException){
            throw e
        }finally {
            viewModelScope.launch{
                //wallpaperState.value = null
                delay(500)
                navController.navigate(Screen.HomeScreen.route) {
                    // Geri tuşuna basıldığında geri dönülmemesi için stack'i temizle
                    popUpTo(Screen.HomeScreen.route) {
                        // Bu işlemi gerçekleştirdiğinizde HomeScreen'e ilk kez gider
                        // ve geri tuşuna basıldığında uygulama kapanır.
                        inclusive = true
                    }
                }
            }
        }
    }

    fun reportWallpaper(report: Report<Wallpaper>){
        try {
            token?.let {
                viewModelScope.launch {
                    wallpaperRepository.createWallpaperReport(report = report, token = it)
                }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun reportUser(report: Report<User>){
        try {
            token?.let {
                viewModelScope.launch{
                    userRepository.createUserReport(report =  report, token = it)
                }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }
}