package com.zeroone.wallpaperdeal.ui.screens.requests

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroone.wallpaperdeal.data.model.WallDeal
import com.zeroone.wallpaperdeal.data.model.WallDealRequest
import com.zeroone.wallpaperdeal.data.remote.repository.WallDealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val wallDealRepository: WallDealRepository
): ViewModel(){
    var requestsState: MutableState<List<WallDealRequest>> = mutableStateOf(emptyList())

    fun fetchRequest(userId: String){
        try {
            viewModelScope.launch {
                requestsState.value = wallDealRepository.getRequestsByUserId(userId = userId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun createWallDeal(wallDeal: WallDeal){
        try {
            viewModelScope.launch {
                wallDealRepository.createWallDeal(wallDeal)
                Log.d("createWalldeal viewmodel:", "success")
            }
        }catch (e: RuntimeException){
            Log.e("createWalldeal viewmodel:", e.message.toString())
            throw e
        }
    }

    fun deleteWallDealRequest(requestId: String){
        try {
            viewModelScope.launch {
                wallDealRepository.deleteRequest(requestId = requestId)
                Log.d("deleteWalldeal viewmodel:", "success")
            }
        }catch (e: RuntimeException){
            Log.e("deleteWalldeal viewmodel:", e.message.toString())
            throw e
        }
    }

    fun addUserToWallDeal(userId: String, otherUserId: String){
        try {
            viewModelScope.launch {
                wallDealRepository.addUserToWallDeal(userId = userId, otherUserId = otherUserId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }
}