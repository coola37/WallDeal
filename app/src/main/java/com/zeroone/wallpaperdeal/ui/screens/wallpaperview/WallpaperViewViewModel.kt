package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroone.wallpaperdeal.model.LikeRequest
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.repository.WallpaperRepository
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
    private val wallpaperRepository: WallpaperRepository
): ViewModel(){

    val wallpaperState : MutableState<Wallpaper?> = mutableStateOf(null)
    val checkLikeState : MutableState<Boolean> = mutableStateOf(false)
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
}