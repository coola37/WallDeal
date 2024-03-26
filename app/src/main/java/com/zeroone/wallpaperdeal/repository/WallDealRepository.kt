package com.zeroone.wallpaperdeal.repository

import com.zeroone.wallpaperdeal.model.WallDeal
import com.zeroone.wallpaperdeal.model.WallDealRequest
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.model.WallpaperRequest

interface WallDealRepository {
    suspend fun sendWallDealRequest(senderUser: String, receiverUser: String)
    suspend fun deleteRequest(requestId: String) : String
    suspend fun checkWallDealRequest(currentUserId: String, targetUserId: String) : Boolean
    suspend fun checkWalldeal(targetUserId: String) : Boolean
    suspend fun checkWalldealForBetweenUserToUser(currentUserId: String, targetUserId: String) : Boolean
    suspend fun sendWallpaperRequest(currentUserId: String, request: WallpaperRequest)
    suspend fun checkWallDealRequests(currentUserId: String) : Boolean
    suspend fun getRequestsByUserId(userId: String) : List<WallDealRequest>
    suspend fun getWallDeal(userId: String) : WallDeal
    suspend fun cancelWallpaperRequest(currentUserId: String, request: WallpaperRequest)
}