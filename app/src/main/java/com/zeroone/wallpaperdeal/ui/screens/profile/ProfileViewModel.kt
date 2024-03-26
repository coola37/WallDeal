package com.zeroone.wallpaperdeal.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.model.User
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.repository.UserRepository
import com.zeroone.wallpaperdeal.repository.WallDealRepository
import com.zeroone.wallpaperdeal.repository.WallpaperRepository
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
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val wallpaperRepository: WallpaperRepository,
    private val userRepository: UserRepository,
    private val wallDealRepository: WallDealRepository
) : ViewModel() {
    data class ItemsState(
        val wallpapers: List<Wallpaper> = emptyList(),
        val user: User? = null
    )

    var stateItems = mutableStateOf<ItemsState>(ItemsState())
    private var job: Job? = null

    val wallDealRequestState: MutableState<Boolean> = mutableStateOf(false)
    val wallDealForTargetUserState: MutableState<Boolean> = mutableStateOf(false)
    val wallDealForBetweenUserToUserState: MutableState<Boolean> = mutableStateOf(false)

    fun fetchItems(userId: String) {
        job?.cancel()
        job = getItems(userId).onEach {
            stateItems.value = it
        }.launchIn(viewModelScope)
    }

    private fun getItems(userId: String): Flow<ItemsState> = flow {
        try {
            val wallpapers = wallpaperRepository.getWallpapersByOwner(userId)
            val user = userRepository.getUser(userId)

            emit(ItemsState(wallpapers = wallpapers.payload, user = user))
        } catch (e: IOError) {
            Log.e("SearchViewModel getItems error:", e.message.toString())
        }
    }

     fun sendWallDealRequest(senderUserId: String, receiverUserId: String){
        try {
            viewModelScope.launch {
                wallDealRepository.sendWallDealRequest(senderUser = senderUserId, receiverUser = receiverUserId)
                wallDealRequestState.value = true
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun checkWallDealRequest(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch {
                wallDealRequestState.value = wallDealRepository.checkWallDealRequest(currentUserId = currentUserId, targetUserId = targetUserId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun checkWallDeal(targetUserId: String){
        try {
            viewModelScope.launch {
                wallDealForTargetUserState.value = wallDealRepository.checkWalldeal(targetUserId = targetUserId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun checkWallDealForBetweenUserToUser(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch{
                wallDealForBetweenUserToUserState.value = wallDealRepository.checkWalldealForBetweenUserToUser(
                    currentUserId = currentUserId, targetUserId = targetUserId
                )
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

}