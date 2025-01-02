package com.zeroone.wallpaperdeal.data.remote.repository.impl

import com.zeroone.wallpaperdeal.data.remote.api.WallDealAPI
import com.zeroone.wallpaperdeal.data.model.Couple
import com.zeroone.wallpaperdeal.data.model.CoupleRequest
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest
import com.zeroone.wallpaperdeal.data.remote.repository.WallDealRepository
import javax.inject.Inject

@Suppress("UNREACHABLE_CODE")
class WallDealRepositoryImpl @Inject constructor(private val api: WallDealAPI) :
    WallDealRepository {
    override suspend fun sendWallDealRequest(token:String, senderUser: String, receiverUser: String) {
        try{
            val bearer = "Bearer $token"
            api.sendWallDealRequest(senderUser = senderUser, receiverUser = receiverUser, token = bearer) }
        catch (ex: RuntimeException){ throw ex }
    }

    override suspend fun deleteRequest(token:String, requestId: String){
        try {
            val bearer = "Bearer $token"
            api.deleteRequest(requestId = requestId, token = bearer)
        }catch (ex: RuntimeException){
            throw ex
        }
    }

    override suspend fun checkWallDealRequest(
        token:String,
        currentUserId: String,
        targetUserId: String
    ): Boolean {
        try {
            val bearer = "Bearer $token"
            return api.checkWallDealRequest(currentUserId = currentUserId, targetUserId = targetUserId, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun checkWalldeal(token:String, targetUserId: String): Boolean {
        try {
            val bearer = "Bearer $token"
            return api.checkWalldeal(targetUserId = targetUserId, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun checkWalldealForBetweenUserToUser(
        token:String,
        currentUserId: String,
        targetUserId: String
    ): Boolean {
        try {
            val bearer = "Bearer $token"
            return api.checkWalldealForBetweenUserToUser(currentUserId = currentUserId, targetUserId = targetUserId, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun sendWallpaperRequest(token:String, currentUserId: String, request: WallpaperRequest) {
        try {
            val bearer = "Bearer $token"
            api.sendPost(currentUserId = currentUserId, request = request, token=bearer )
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun checkWallDealRequests(token:String, currentUserId: String) : Boolean{
        try {
            val bearer = "Bearer $token"
            return api.checkWallDealRequests(userId = currentUserId, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun getRequestsByUserId(token:String, userId: String) : List<CoupleRequest>{
        try {
            val bearer = "Bearer $token"
            return api.getRequestsByUserId(userId = userId, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun getWallDeal(token:String, userId: String): Couple {
        try {
            val bearer = "Bearer $token"
            return api.getWallDeal(userId = userId, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun cancelWallpaperRequest(token:String, currentUserId: String, request: WallpaperRequest) {
        try {
            val bearer = "Bearer $token"
            api.cancelPost(currentUserId = currentUserId, request = request, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun createWallDeal(token:String, couple: Couple) {
        try {
            val bearer = "Bearer $token"
            api.createWallDeal(couple = couple, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun cancelWallDeal(token:String, userId: String) {
        val bearer = "Bearer $token"
        try {
            api.cancelWallDeal(userId = userId, token = bearer)
        } catch (e: RuntimeException) {
            throw e
        }
    }

    override suspend fun getWallpaperRequest(
        token:String,
        requestId: String
    ): WallpaperRequest {
        val bearer = "Bearer $token"
        try {
            return api.getWallpaperRequest(token= bearer, requestId = requestId)
        }catch (e: RuntimeException){
            throw e
        }
    }
}