package com.zeroone.wallpaperdeal.ui.screens.share

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.model.WallpapersState
import com.zeroone.wallpaperdeal.repository.UserRepository
import com.zeroone.wallpaperdeal.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val userRepository: UserRepository
) : ViewModel(){
    private val _state = mutableStateOf<WallpapersState>(WallpapersState())
    val state : State<WallpapersState> = _state
    val loadingState = mutableStateOf<Boolean>(false)
    val successState = mutableStateOf<Boolean?>(false)

    private var job: Job? = null

    suspend fun shareWallpaper(wallpaper: Wallpaper, userId: String) {
        loadingState.value = true
        successState.value = false

        val user = userRepository.getUser(userId)
        val userDTO = userRepository.userConverToUserDTO(user = user!!)
        try{
            val post = Wallpaper(
                wallpaper.wallpaperId,
                wallpaper.description,
                userDTO,
                wallpaper.imageUrl,
                wallpaper.category,
                wallpaper.gradiantUrl,
                emptyList(),
                0
            )
            wallpaperRepository.saveWallpaper(post)
            successState.value = true
            loadingState.value = false
        }catch (e: RuntimeException){
            loadingState.value = false
            successState.value = false
            Log.e("ShareWallpaper", e.message.toString())
        }
    }
}