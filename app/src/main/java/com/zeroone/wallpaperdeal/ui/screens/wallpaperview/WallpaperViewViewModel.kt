package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.data.response.ResponseWallpaper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WallpaperViewViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
): ViewModel(){

    val wallpaperState : MutableState<Wallpaper?> = mutableStateOf(null)
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
}