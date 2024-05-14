package com.zeroone.wallpaperdeal.ui.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.local.WallpaperDatabase
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.model.WallpapersState
import com.zeroone.wallpaperdeal.data.remote.repository.WallDealRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOError
import javax.inject.Inject

@Suppress("UNREACHABLE_CODE")
@HiltViewModel
class HomeViewModel @Inject constructor(
    auth: FirebaseAuth,
    private val wallpaperRepository: WallpaperRepository,
    private val wallDealRepository: WallDealRepository,
    private val wallpaperDatabase: WallpaperDatabase
) : ViewModel() {
    var state = mutableStateOf<WallpapersState>(WallpapersState())
    var requestsState: MutableState<Boolean> = mutableStateOf(false)
    var wallpaperStateFromLocal: MutableState<List<Wallpaper>> = mutableStateOf(emptyList())
    private var job: Job? = null

    init{
        auth.uid?.let {
            getAllWallpapers(currentUserId = it)
        }
    }
    fun getWallpapersFromLocal(){
        viewModelScope.launch {
            wallpaperStateFromLocal.value = wallpaperDatabase.wallpaperDao().getAllWallpapers()
        }
    }
    fun getAllWallpapers(currentUserId: String) {
        job?.cancel()
        job = getWallpaper(currentUserId = currentUserId).onEach {
            when (it) {
                is Resource.Success -> {
                    state.value = WallpapersState(wallpapers = it.data ?: emptyList())
                    syncWallpapers(it.data ?: emptyList())
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

    private fun getWallpaper(currentUserId: String): Flow<Resource<List<Wallpaper>>> = flow {
        try {
            emit(Resource.Loading())
            val wallpaperList = wallpaperRepository.getWallpaperByFollowed(currentUserId = currentUserId)
            if (wallpaperList.response == "Succes") {
                emit(Resource.Success(wallpaperList.payload))
            } else {
                emit(Resource.Error(message = "Error"))
            }
        } catch (e: IOError) {
            emit(Resource.Error(message = "No internet connection!"))
        }
    }

    private fun syncWallpapers(remoteWallpapers: List<Wallpaper>) {
        viewModelScope.launch {
            val localWallpapers = wallpaperDatabase.wallpaperDao().getAllWallpapers()

            //Adds wallpapers that are not in the local database.
            for (remoteWallpaper in remoteWallpapers) {
                if (!localWallpapers.any { it.wallpaperId == remoteWallpaper.wallpaperId }) {
                    wallpaperDatabase.wallpaperDao().insertWallpaper(remoteWallpaper)
                    Log.d("SyncWallpapers", "Wallpaper with ID ${remoteWallpaper.wallpaperId} inserted.")
                } else {
                    Log.d("SyncWallpapers", "Wallpaper with ID ${remoteWallpaper.wallpaperId} already exists.")
                }
            }

            // Delete wallpapers that are  in the local database but that are not in remote.
            for (localWallpaper in localWallpapers) {
                if (!remoteWallpapers.any { it.wallpaperId == localWallpaper.wallpaperId }) {
                    wallpaperDatabase.wallpaperDao().deleteWallpaper(localWallpaper)
                    Log.d("SyncWallpapers", "Wallpaper with ID ${localWallpaper.wallpaperId} deleted.")
                }
            }
        }
    }


    fun checkRequestForUser(userId: String) {
        try {
            viewModelScope.launch {
                requestsState.value = wallDealRepository.checkWallDealRequests(currentUserId = userId)
                val request = wallDealRepository.checkWallDealRequests(currentUserId = userId)
                //Log.e("Request viewmodel Control", request.toString())
            }
        } catch (e: RuntimeException) {
            throw e
            Log.e("checkRequestForUser", e.message.toString())
        }
    }
}
