package com.zeroone.wallpaperdeal.ui.screens.search

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroone.wallpaperdeal.model.User
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.model.WallpapersState
import com.zeroone.wallpaperdeal.repository.UserRepository
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
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val wallpaperRepository: WallpaperRepository,
) : ViewModel() {
    data class ItemsState(
        val wallpapers: List<Wallpaper> = emptyList(),
        val users: List<User> = emptyList()
    )

    var stateItems = mutableStateOf<ItemsState>(ItemsState())
    private var job: Job? = null

    fun fetchItems() {
        job?.cancel()
        job = getItems().onEach {
            stateItems.value = it
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
}