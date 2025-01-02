package com.zeroone.wallpaperdeal.ui.screens.requests

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroone.wallpaperdeal.data.model.Couple
import com.zeroone.wallpaperdeal.data.model.CoupleRequest
import com.zeroone.wallpaperdeal.data.remote.repository.WallDealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val wallDealRepository: WallDealRepository,
    @ApplicationContext context: Context
): ViewModel(){
    var requestsState: MutableState<List<CoupleRequest>> = mutableStateOf(emptyList())
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val token = sharedPreferences.getString("firebase_token", null)

    fun fetchRequest(userId: String){
        try {
            token?.let {
                viewModelScope.launch {
                    requestsState.value = wallDealRepository.getRequestsByUserId(userId = userId, token = it)
                }
            }

        }catch (e: RuntimeException){
            throw e
        }
    }

    fun createWallDeal(couple: Couple){
        try {
            token?.let {
                viewModelScope.launch {
                    wallDealRepository.createWallDeal(couple = couple, token = it)
                    Log.d("createWalldeal viewmodel:", "success")
                }
            }
        }catch (e: RuntimeException){
            Log.e("createWalldeal viewmodel:", e.message.toString())
            throw e
        }
    }

    fun deleteWallDealRequest(requestId: String){
        try {
            token?.let {
                viewModelScope.launch {
                    wallDealRepository.deleteRequest(requestId = requestId, token = it)
                    Log.d("deleteWalldeal viewmodel:", "success")
                }
            }
        }catch (e: RuntimeException){
            Log.e("deleteWalldeal viewmodel:", e.message.toString())
            throw e
        }
    }

}