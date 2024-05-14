package com.zeroone.wallpaperdeal.ui.screens.couple

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.model.WallDeal
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest
import com.zeroone.wallpaperdeal.data.remote.repository.WallDealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoupleOprtViewModel @Inject constructor(
    private val wallDealRepository: WallDealRepository,
    private val auth: FirebaseAuth
) : ViewModel(){

    var wallDealState: MutableState<WallDeal?> = mutableStateOf(null)
    var requestState: MutableState<WallpaperRequest?> = mutableStateOf(null)

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

    fun getWallpaperRequest(requestId: String){
        try {
            viewModelScope.launch {
                requestState.value = wallDealRepository.getWallpaperRequest(requestId = requestId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }
}