package com.zeroone.wallpaperdeal.data.remote.repository.impl

import android.util.Log
import com.zeroone.wallpaperdeal.data.remote.api.WallDealAPI
import com.zeroone.wallpaperdeal.data.model.WallDeal
import com.zeroone.wallpaperdeal.data.model.WallDealRequest
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest
import com.zeroone.wallpaperdeal.data.remote.repository.WallDealRepository
import javax.inject.Inject

@Suppress("UNREACHABLE_CODE")
class WallDealRepositoryImpl @Inject constructor(private val api: WallDealAPI) :
    WallDealRepository {
    override suspend fun sendWallDealRequest(senderUser: String, receiverUser: String) {
        try{ api.sendWallDealRequest(senderUser = senderUser, receiverUser = receiverUser) }
        catch (ex: RuntimeException){ throw ex }
    }

    override suspend fun deleteRequest(requestId: String){
        try {
            api.deleteRequest(requestId = requestId)
        }catch (ex: RuntimeException){
            throw ex
        }
    }

    override suspend fun checkWallDealRequest(
        currentUserId: String,
        targetUserId: String
    ): Boolean {
        try {
            return api.checkWallDealRequest(currentUserId = currentUserId, targetUserId = targetUserId)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun checkWalldeal(targetUserId: String): Boolean {
        try {
            return api.checkWalldeal(targetUserId = targetUserId)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun checkWalldealForBetweenUserToUser(
        currentUserId: String,
        targetUserId: String
    ): Boolean {
        try {
            return api.checkWalldealForBetweenUserToUser(currentUserId = currentUserId, targetUserId = targetUserId)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun sendWallpaperRequest(currentUserId: String, request: WallpaperRequest) {
        try {
            api.sendPost(currentUserId = currentUserId, request = request)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun checkWallDealRequests(currentUserId: String) : Boolean{
        try {
            return api.checkWallDealRequests(userId = currentUserId)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun getRequestsByUserId(userId: String) : List<WallDealRequest>{
        try {
            return api.getRequestsByUserId(userId = userId)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun getWallDeal(userId: String): WallDeal {
        try {
            return api.getWallDeal(userId = userId)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun cancelWallpaperRequest(currentUserId: String, request: WallpaperRequest) {
        try {
            api.cancelPost(currentUserId = currentUserId, request = request)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun createWallDeal(wallDeal: WallDeal) {
        try {
            api.createWallDeal(wallDeal = wallDeal)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun cancelWallDeal(userId: String) {
        try {
            api.cancelWallDeal(userId = userId)
        } catch (e: RuntimeException) {
            throw e
        }
    }

    override suspend fun addUserToWallDeal(userId: String, otherUserId: String) {
        try {
            api.addUserToWallDeal(userId = userId, otherUserId = otherUserId)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun getWallpaperRequest(
        requestId: String
    ): WallpaperRequest {
        try {
            return api.getWallpaperRequest(requestId = requestId)
        }catch (e: RuntimeException){
            throw e
        }
    }
}