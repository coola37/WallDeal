package com.zeroone.wallpaperdeal.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
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
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val wallpaperRepository: WallpaperRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    data class ItemsState(
        val wallpapers: List<Wallpaper> = emptyList(),
        val user: User? = null
    )

    var stateItems = mutableStateOf<ItemsState>(ItemsState())
    private var job: Job? = null

    fun fetchItems(userId: String) {
        job?.cancel()
        job = getItems(userId).onEach {
            stateItems.value = it
        }.launchIn(viewModelScope)
    }

    private fun getItems(userId: String): Flow<ItemsState> = flow {
        try {
            val wallpapers = wallpaperRepository.getWallpapers()
            val user = userRepository.getUser(userId)

            emit(ItemsState(wallpapers = wallpapers.payload, user = user))
        } catch (e: IOError) {
            Log.e("SearchViewModel getItems error:", e.message.toString())
        }
    }

}