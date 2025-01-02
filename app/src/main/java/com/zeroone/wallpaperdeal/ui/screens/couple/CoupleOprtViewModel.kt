package com.zeroone.wallpaperdeal.ui.screens.couple

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.model.Couple
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest
import com.zeroone.wallpaperdeal.data.remote.repository.WallDealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoupleOprtViewModel @Inject constructor(
    private val wallDealRepository: WallDealRepository,
    private val auth: FirebaseAuth,
    @ApplicationContext context: Context
) : ViewModel(){

    var coupleState: MutableState<Couple?> = mutableStateOf(null)
    var requestState: MutableState<WallpaperRequest?> = mutableStateOf(null)
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val token = sharedPreferences.getString("firebase_token", null)

    fun getWallDeal(userId: String){

        try {
            viewModelScope.launch{ coupleState.value = wallDealRepository.getWallDeal(token = token!!, userId = userId) }
        }catch (e: RuntimeException){
            throw e
        }
    }
    fun cancelPost(userId: String, request: WallpaperRequest){
        try {
            viewModelScope.launch {
                wallDealRepository.cancelWallpaperRequest(token = token!!,currentUserId = userId, request = request)
                getWallDeal(userId = userId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun sendWallpaperRequest(userId: String, request: WallpaperRequest){
        try {
            viewModelScope.launch {
                wallDealRepository.sendWallpaperRequest(token = token!!,currentUserId = userId, request = request)
                getWallDeal(userId = userId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun getWallpaperRequest(requestId: String){
        try {
            viewModelScope.launch {
                requestState.value = wallDealRepository.getWallpaperRequest(token = token!!,requestId = requestId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }
}