package com.zeroone.wallpaperdeal.ui.screens.walldeal

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.model.WallDeal
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.model.WallpaperRequest
import com.zeroone.wallpaperdeal.repository.WallDealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallDealViewModel @Inject constructor(
    private val wallDealRepository: WallDealRepository,
    private val auth: FirebaseAuth
) : ViewModel(){

    var wallDealState: MutableState<WallDeal?> = mutableStateOf(null)

    fun getWallDeal(userId: String){
        try {
            viewModelScope.launch{ wallDealState.value = wallDealRepository.getWallDeal(userId = userId) }
        }catch (e: RuntimeException){
            throw e
        }
    }
    fun cancelPost(userId: String, request: WallpaperRequest){
        try {
            viewModelScope.launch {
                wallDealRepository.cancelWallpaperRequest(currentUserId = userId, request = request)
                getWallDeal(userId = userId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun sendWallpaperRequest(userId: String, request: WallpaperRequest){
        try {
            viewModelScope.launch {
                wallDealRepository.sendWallpaperRequest(currentUserId = userId, request = request)
                getWallDeal(userId = userId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }
}