package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroone.wallpaperdeal.data.model.LikeRequest
import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val userRepository: UserRepository
): ViewModel(){

    val wallpaperState : MutableState<Wallpaper?> = mutableStateOf(null)
    val checkLikeState : MutableState<Boolean> = mutableStateOf(false)
    val checkFavoritesState : MutableState<Boolean> = mutableStateOf(false)
    private var job: Job? = null

    fun fetchWallpaper(wallpaperId: String){
        job?.cancel()
        Log.e("Wallpaper Fetched", "true")
        job = getWallpaper(wallpaperId = wallpaperId).onEach {
            wallpaperState.value = it
        }.launchIn(viewModelScope)
    }
   private fun getWallpaper(wallpaperId: String): Flow<Wallpaper> = flow{
        try {
            val wallpaper = wallpaperRepository.getWallpaperById(wallpaperId = wallpaperId)
            emit(wallpaper)
        }catch (e: IOException){
            throw e
        }
   }

    fun likeOrDislike(wallpaperId: String, likeRequest: LikeRequest){
        try {
            viewModelScope.launch {
                wallpaperRepository.likeOrDislike(wallpaperId = wallpaperId, likeRequest = likeRequest)
                Log.e("likeOrDislike:", "succes")
                checkLikeState.value = !checkLikeState.value
                fetchWallpaper(wallpaperId = wallpaperId)
            }
        }catch (ex: IOException){
            Log.e("likeOrDislike Error:", ex.message.toString())
        }
    }

    fun checkLike(wallpaperId: String, userId: String){
        try {
            viewModelScope.launch{
                checkLikeState.value = wallpaperRepository.checkLike(wallpaperId = wallpaperId, currentUserId = userId)
                Log.e("checkLike ViewModel:", checkLikeState.value.toString())
            }
        }catch (ex: IOException){
            Log.e("CheckLike Error:", ex.message.toString())
        }
    }

    fun addOrRemoveFavorites(wallpaperId: String, userId: String){
        try {
            viewModelScope.launch {
                wallpaperRepository.addFavorite(wallpaperId = wallpaperId, userId = userId)
                checkFavoritesState.value = !checkFavoritesState.value
            }
        }catch (ex: IOException){
            Log.e("addOrRemoveFavorites error:", ex.message.toString())
        }
    }

    fun checkFavorites(userId: String, wallpaperId: String){
        try {
            viewModelScope.launch {
                checkFavoritesState.value = userRepository.checkFavorites(userId = userId, wallpaperId = wallpaperId)
                Log.e("checkFavorites in viewmodel state", userRepository.checkFavorites(userId = userId, wallpaperId = wallpaperId).toString())
            }
        }catch (e: RuntimeException){
            Log.e("checkFavorites in viewmodel", e.message.toString())
            throw e
        }
    }

    fun removeWallpaper(wallpaperId: String){
        try{
            viewModelScope.launch {
                wallpaperRepository.removeWallpaper(wallpaperId = wallpaperId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun reportWallpaper(report: Report<Wallpaper>){
        try {
            viewModelScope.launch {
                wallpaperRepository.createWallpaperReport(report = report)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun reportUser(report: Report<User>){
        try {
            viewModelScope.launch{
                userRepository.createUserReport(report =  report)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

}