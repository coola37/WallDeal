package com.zeroone.wallpaperdeal.data.remote.repository

import com.zeroone.wallpaperdeal.data.model.WallDeal
import com.zeroone.wallpaperdeal.data.model.WallDealRequest
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface WallDealRepository {
    suspend fun sendWallDealRequest(senderUser: String, receiverUser: String)
    suspend fun deleteRequest(requestId: String)
    suspend fun checkWallDealRequest(currentUserId: String, targetUserId: String) : Boolean
    suspend fun checkWalldeal(targetUserId: String) : Boolean
    suspend fun checkWalldealForBetweenUserToUser(currentUserId: String, targetUserId: String) : Boolean
    suspend fun sendWallpaperRequest(currentUserId: String, request: WallpaperRequest)
    suspend fun checkWallDealRequests(currentUserId: String) : Boolean
    suspend fun getRequestsByUserId(userId: String) : List<WallDealRequest>
    suspend fun getWallDeal(userId: String) : WallDeal
    suspend fun cancelWallpaperRequest(currentUserId: String, request: WallpaperRequest)
    suspend fun createWallDeal(wallDeal: WallDeal)
    suspend fun cancelWallDeal(userId: String)
    suspend fun addUserToWallDeal(userId: String,otherUserId: String)
    suspend fun getWallpaperRequest(requestId: String) : WallpaperRequest

}