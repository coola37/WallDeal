package com.zeroone.wallpaperdeal.data.remote.repository

import com.zeroone.wallpaperdeal.data.model.Couple
import com.zeroone.wallpaperdeal.data.model.CoupleRequest
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest

interface WallDealRepository {
    suspend fun sendWallDealRequest(token:String, senderUser: String, receiverUser: String)
    suspend fun deleteRequest(token:String, requestId: String)
    suspend fun checkWallDealRequest(token:String, currentUserId: String, targetUserId: String) : Boolean
    suspend fun checkWalldeal(token:String, targetUserId: String) : Boolean
    suspend fun checkWalldealForBetweenUserToUser(token:String, currentUserId: String, targetUserId: String) : Boolean
    suspend fun sendWallpaperRequest(token:String, currentUserId: String, request: WallpaperRequest)
    suspend fun checkWallDealRequests(token:String, currentUserId: String) : Boolean
    suspend fun getRequestsByUserId(token:String, userId: String) : List<CoupleRequest>
    suspend fun getWallDeal(token:String, userId: String) : Couple
    suspend fun cancelWallpaperRequest(token:String, currentUserId: String, request: WallpaperRequest)
    suspend fun createWallDeal(token:String, couple: Couple)
    suspend fun cancelWallDeal(token:String, userId: String)
    suspend fun getWallpaperRequest(token:String, requestId: String) : WallpaperRequest

}