package com.zeroone.wallpaperdeal.data.remote.repository

import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.UserDTO
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Path


interface UserRepository {
    suspend fun saveUser(token:String, user: User): String
    suspend fun getUser(token:String, userId: String): User?
    suspend fun getUsers(token:String): List<User>
    suspend fun checkFavorites(token:String, userId: String, wallpaperId: Int) : Boolean
    suspend fun followOrUnfollow(token:String, currentUserId: String,targetUserId: String)
    suspend fun checkFollow(token:String, currentUserId: String, targetUserId: String): Boolean
    fun userConverToUserDTO(user: User): UserDTO
    suspend fun editProfile(token:String, user: User)
    suspend fun createUserReport(token:String, report: Report<User>)
    suspend fun deleteAccount(token:String, userId: String)
    suspend fun checkUser(token:String, userId: String): Boolean
    suspend fun checkUsingUsername(username:String): Boolean
}