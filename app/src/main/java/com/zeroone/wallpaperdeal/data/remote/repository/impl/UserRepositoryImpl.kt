package com.zeroone.wallpaperdeal.data.remote.repository.impl

import android.util.Log
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.remote.api.UserAPI
import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.UserDTO
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import retrofit2.http.Header
import java.io.EOFException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: UserAPI): UserRepository {
    override suspend fun saveUser(token:String, user: User): String {
        try{
            val bearer = "Bearer $token"
            api.saveUser(user = user, token = bearer)
            return "User has been created"
        }catch (e: EOFException){
            Log.e("saveUserError:", e.message.toString())
            return "error"
        }
    }

    override suspend fun getUser(token:String, userId: String) : User? {
        try{
            val bearer = "Bearer $token"
            Log.e("GetUserRepository token:", bearer)
            return api.getUser(userId = userId, token = bearer)
        }catch (e: RuntimeException) {
            Log.e("getUserError:", e.message.toString())
            throw e
        }
    }


    override suspend fun getUsers(token:String): List<User> {
        try{
            val bearer = "Bearer $token"
            return api.getUsers(bearer).toList()
        }catch (e: RuntimeException){
            Log.e("getUsers", e.message.toString())
            throw e
        }
    }

    override suspend fun checkFavorites(token:String, userId: String, wallpaperId: Int): Boolean {
        try {
            val bearer = "Bearer $token"
            return api.checkFavorites(userId = userId, wallpaperId = wallpaperId, token = bearer)
        }catch (e: RuntimeException){
            Log.e("checkFavorites", e.message.toString())
            throw e
        }
    }

    override suspend fun followOrUnfollow(token:String, currentUserId: String, targetUserId: String) {
        try {
            val bearer = "Bearer $token"
            return api.followOrUnfollow(currentUserId = currentUserId, targetUserId = targetUserId,token = bearer, )
        }catch (e: RuntimeException){
            Log.e("followOrUnfollow", e.message.toString())
            throw e
        }
    }

    override suspend fun checkFollow(token:String, currentUserId: String, targetUserId: String): Boolean {
        try {
            val bearer = "Bearer $token"
            return api.checkFollow(currentUserId = currentUserId, targetUserId = targetUserId, token = bearer)
        }catch (e: RuntimeException){
            Log.e("checkFollow", e.message.toString())
            throw e
        }
    }

    override fun userConverToUserDTO(user: User): UserDTO {
        val dto = UserDTO(
            userId = user.userId,
            email = user.email,
            profilePhoto = user.profilePhoto,
            wallDealId = user.coupleId,
            username = user.username,
            fcmToken = user.fcmToken
        )
        return dto
    }

    override suspend fun editProfile(token:String, user: User) {
        val bearer = "Bearer $token"
        try {
            api.editProfile(user = user, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun createUserReport(token:String, report: Report<User>) {
        val bearer = "Bearer $token"
        try {
            api.createUserReport(report = report, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun deleteAccount(token:String, userId: String) {
        val bearer = "Bearer $token"
        try {
            api.deleteAccount(userId = userId, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun checkUser(token:String, userId: String): Boolean{
        val bearer = "Bearer $token"
        try {
            return api.checkUser(userId = userId, token = bearer)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun checkUsingUsername(username: String): Boolean {
        return api.checkUsedUsername(username = username)
    }
}