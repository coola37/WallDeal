package com.zeroone.wallpaperdeal.ui.screens.share

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val userRepository: UserRepository
) : ViewModel(){
    private val _state = mutableStateOf<WallpaperState>(WallpaperState())
    val state : State<WallpaperState> = _state

    private var job: Job? = null

    suspend fun shareWallpaper(wallpaper: Wallpaper, userId: String){
        val user = userRepository.getUser(userId)
        try{
            val post = Wallpaper(
                wallpaper.wallpaperId,
                wallpaper.description,
                user,
                wallpaper.imageUrl,
                wallpaper.category,
                null,
                emptyList(),
                0
            )
            wallpaperRepository.saveWallpaper(post)
        }catch (e: RuntimeException){
            Log.e("ShareWallpaper", e.message.toString())
        }
    }
}