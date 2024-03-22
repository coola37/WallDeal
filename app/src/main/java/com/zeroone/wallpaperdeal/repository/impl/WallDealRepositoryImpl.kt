package com.zeroone.wallpaperdeal.repository.impl

import com.zeroone.wallpaperdeal.api.WallDealAPI
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.repository.WallDealRepository
import javax.inject.Inject

class WallDealRepositoryImpl @Inject constructor( private val api: WallDealAPI) : WallDealRepository {
    override suspend fun sendWallDealRequest(senderUser: String, receiverUser: String) {
        try{ api.sendWallDealRequest(senderUser = senderUser, receiverUser = receiverUser) }
        catch (ex: RuntimeException){ throw ex }
    }

    override suspend fun deleteRequest(requestId: String): String {
        try {
            return api.deleteRequest(requestId = requestId)
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

    override suspend fun sendPost(currentUserId: String, post: Wallpaper) {
        try {
            api.sendPost(currentUserId = currentUserId, post = post)
        }catch (e: RuntimeException){
            throw e
        }
    }
}