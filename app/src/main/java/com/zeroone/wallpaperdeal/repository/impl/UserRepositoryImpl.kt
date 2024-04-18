package com.zeroone.wallpaperdeal.repository.impl

import android.util.Log
import com.zeroone.wallpaperdeal.model.User
import com.zeroone.wallpaperdeal.api.UserAPI
import com.zeroone.wallpaperdeal.model.Report
import com.zeroone.wallpaperdeal.model.ResponseWallpaper
import com.zeroone.wallpaperdeal.model.UserDTO
import com.zeroone.wallpaperdeal.repository.UserRepository
import java.io.EOFException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: UserAPI): UserRepository {
    override suspend fun saveUser(user: User): String {
        try{
            api.saveUser(user = user)
            return "User has been created"
        }catch (e: EOFException){
            Log.e("saveUserError:", e.message.toString())
            return "error"
        }
    }

    override suspend fun getUser(userId: String) : User? {
        try{
            return api.getUser(userId = userId)
        }catch (e: RuntimeException) {
            Log.e("getUserError:", e.message.toString())
            throw e
        }
    }

    override suspend fun getUsers(): List<User> {
        try{
            return api.getUsers()
        }catch (e: RuntimeException){
            Log.e("getUsers", e.message.toString())
            throw e
        }
    }

    override suspend fun checkFavorites(userId: String, wallpaperId: String): Boolean {
        try {
            return api.checkFavorites(userId = userId, wallpaperId = wallpaperId)
        }catch (e: RuntimeException){
            Log.e("checkFavorites", e.message.toString())
            throw e
        }
    }

    override suspend fun followOrUnfollow(currentUserId: String, targetUserId: String) {
        try {
            return api.followOrUnfollow(currentUserId = currentUserId, targetUserId = targetUserId)
        }catch (e: RuntimeException){
            Log.e("followOrUnfollow", e.message.toString())
            throw e
        }
    }

    override suspend fun checkFollow(currentUserId: String, targetUserId: String): Boolean {
        try {
            return api.checkFollow(currentUserId = currentUserId, targetUserId = targetUserId)
        }catch (e: RuntimeException){
            Log.e("checkFollow", e.message.toString())
            throw e
        }
    }

    override fun userConverToUserDTO(user: User): UserDTO {
        val dto = UserDTO(
            userId = user.userId,
            email = user.email,
            profilePhoto = user.userDetail?.profilePhoto,
            wallDealId = user.wallDealId,
            username = user.username,
            fcmToken = user.fcmToken
        )
        return dto
    }

    override suspend fun editProfile(user: User) {
        try {
            api.editProfile(user = user)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun createUserReport(report: Report<User>) {
        try {
            api.createUserReport(report = report)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun deleteAccount(userId: String) {
        try {
            api.deleteAccount(userId = userId)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun checkUser(userId: String): Boolean{
        try {
            return api.checkUser(userId = userId)
        }catch (e: RuntimeException){
            throw e
        }
    }
}