package com.zeroone.wallpaperdeal.repository

import com.zeroone.wallpaperdeal.model.Wallpaper

interface WallDealRepository {
    suspend fun sendWallDealRequest(senderUser: String, receiverUser: String)
    suspend fun deleteRequest(requestId: String) : String
    suspend fun checkWallDealRequest(currentUserId: String, targetUserId: String) : Boolean
    suspend fun checkWalldeal(targetUserId: String) : Boolean
    suspend fun checkWalldealForBetweenUserToUser(currentUserId: String, targetUserId: String) : Boolean
    suspend fun sendPost(currentUserId: String, post: Wallpaper)
}