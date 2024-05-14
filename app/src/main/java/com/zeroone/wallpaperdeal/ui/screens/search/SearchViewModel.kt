package com.zeroone.wallpaperdeal.ui.screens.search

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroone.wallpaperdeal.data.local.UserDatabase
import com.zeroone.wallpaperdeal.data.local.WallpaperDatabase
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
import java.io.IOError
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val wallpaperRepository: WallpaperRepository,
    private val wallpaperDatabase: WallpaperDatabase,
    private val userDatabase: UserDatabase
) : ViewModel() {

    init {
        fetchItems()
    }
    data class ItemsState(
        var wallpapers: List<Wallpaper> = emptyList(),
        var users: List<User> = emptyList()
    )

    var stateItems = mutableStateOf<ItemsState>(ItemsState())
    var localItems = mutableStateOf<ItemsState>(ItemsState())
    private var job: Job? = null

    fun getLocalItems(){
        viewModelScope.launch {
            localItems.value.users = userDatabase.userDao().getAllUsers()
            localItems.value.wallpapers = wallpaperDatabase.wallpaperDao().getAllWallpapers()
        }
    }
    fun fetchItems() {
        job?.cancel()
        job = getItems().onEach {
            stateItems.value = it
            syncWallpapersAndUsers(it.wallpapers, it.users)
        }.launchIn(viewModelScope)
    }

    private fun getItems(): Flow<ItemsState> = flow {
        try {
            val wallpapers = wallpaperRepository.getWallpapers()
            val users = userRepository.getUsers()

            emit(ItemsState(wallpapers = wallpapers.payload, users = users))

        } catch (e: IOError) {
            Log.e("SearchViewModel getItems error:", e.message.toString())
        }
    }

    private fun syncWallpapersAndUsers(remoteWallpapers: List<Wallpaper>, remoteUsers: List<User>) {
        viewModelScope.launch {
            val localWallpapers = wallpaperDatabase.wallpaperDao().getAllWallpapers()
            val localUsers = userDatabase.userDao().getAllUsers()

            //Adds wallpapers that are not in the local database.
            for (remoteWallpaper in remoteWallpapers) {
                if (!localWallpapers.any { it.wallpaperId == remoteWallpaper.wallpaperId }) {
                    wallpaperDatabase.wallpaperDao().insertWallpaper(remoteWallpaper)
                    Log.d("syncWallpapersAndUsers", "Wallpaper with ID ${remoteWallpaper.wallpaperId} inserted.")
                } else {
                    Log.d("syncWallpapersAndUsers", "Wallpaper with ID ${remoteWallpaper.wallpaperId} already exists.")
                }
            }

            // Delete wallpapers that are  in the local database but that are not in remote.
            for (localWallpaper in localWallpapers) {
                if (!remoteWallpapers.any { it.wallpaperId == localWallpaper.wallpaperId }) {
                    wallpaperDatabase.wallpaperDao().deleteWallpaper(localWallpaper)
                    Log.d("syncWallpapersAndUsers", "Wallpaper with ID ${localWallpaper.wallpaperId} deleted.")
                }
            }

            //Adds users that are not in the local database.
            for (remoteUser in remoteUsers) {
                if (!localUsers.any { it.userId == remoteUser.userId }) {
                    userDatabase.userDao().insertUser(remoteUser)
                    Log.d("syncWallpapersAndUsers", "User with ID ${remoteUser.userId} inserted.")
                } else {
                    Log.d("syncWallpapersAndUsers", "User with ID ${remoteUser.userId} already exists.")
                }
            }

            // Delete users that are  in the local database but that are not in remote.
            for (localUser in localUsers) {
                if (!remoteUsers.any { it.userId == localUser.userId }) {
                    userDatabase.userDao().deleteUser(localUser)
                    Log.d("syncWallpapersAndUsers", "User with ID ${localUser.userId} deleted.")
                }
            }
        }
    }
}