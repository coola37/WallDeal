package com.zeroone.wallpaperdeal.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.model.WallpapersState
import com.zeroone.wallpaperdeal.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.IOError
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val wallpaperRepository: WallpaperRepository
) : ViewModel() {
    var state = mutableStateOf<WallpapersState>(WallpapersState())

    private var job : Job? = null

    init {
        getWallpapersByUserId(auth.uid!!)
    }
    fun getWallpapersByUserId(userId: String){
        job?.cancel()
        job = getWallpaper(userId = userId).onEach {
            when(it){
                is Resource.Success -> {
                    state.value = WallpapersState(wallpapers = it.data ?: emptyList())

                }

                is Resource.Error -> {
                    state.value = WallpapersState(error = it.message ?: "Error!")
                }

                is Resource.Loading -> {
                    state.value = WallpapersState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getWallpaper(userId: String) : Flow<Resource<List<Wallpaper>>> = flow {
        try {
            emit(Resource.Loading())
            val wallpaperList = wallpaperRepository.getWallpapersByOwner(userId)
            //Log.e("ProfileViewModel", wallpaperList.payload[1].wallpaperId)
            if(wallpaperList.response.equals("Succes")){
                emit(Resource.Success(wallpaperList.payload))
            }else{
                emit(Resource.Error(message = "Error"))
            }
        }catch (e: IOError){
            emit(Resource.Error(message = "No internet connection!"))
        }
    }
}