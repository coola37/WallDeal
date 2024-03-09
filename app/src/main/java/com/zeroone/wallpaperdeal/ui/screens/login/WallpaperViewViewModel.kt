package com.zeroone.wallpaperdeal.ui.screens.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.data.response.ResponseWallpaper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class WallpaperViewViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
){

    val wallpaperState : MutableState<ResponseWallpaper?> = mutableStateOf(null)

   /* fun getWallpaper(): Flow<Wallpaper>{
        try {
            //wallpaperRepository.getWallpapers()
        }catch (e: IOException){

        }
    }*/
}