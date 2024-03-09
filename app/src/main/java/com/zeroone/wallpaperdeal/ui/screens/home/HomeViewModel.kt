package com.zeroone.wallpaperdeal.ui.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.model.WallpapersState
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
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
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val wallpaperRepository: WallpaperRepository
) : ViewModel() {
    var state = mutableStateOf<WallpapersState>(WallpapersState())
    private var job: Job? = null

    init {
        getAllWallpapers()
    }
    fun getAllWallpapers(){
        job?.cancel()
        job = getWallpaper().onEach {
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

    private fun getWallpaper() : Flow<Resource<List<Wallpaper>>> = flow {
        try {
            emit(Resource.Loading())
            val wallpaperList = wallpaperRepository.getWallpapers()
            if(wallpaperList.response == "Succes"){
                emit(Resource.Success(wallpaperList.payload))
            }else{
                emit(Resource.Error(message = "Error"))
            }
        }catch (e: IOError){
            emit(Resource.Error(message = "No internet connection!"))
        }
    }
}