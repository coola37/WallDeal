package com.zeroone.wallpaperdeal.repository

import com.zeroone.wallpaperdeal.model.Report
import com.zeroone.wallpaperdeal.model.User
import com.zeroone.wallpaperdeal.model.UserDTO
import retrofit2.http.Body
import retrofit2.http.Path


interface UserRepository {
    suspend fun saveUser(user: User): String
    suspend fun getUser(userId: String): User?
    suspend fun getUsers(): List<User>
    suspend fun checkFavorites(userId: String, wallpaperId: String) : Boolean
    suspend fun followOrUnfollow(currentUserId: String,targetUserId: String)
    suspend fun checkFollow(currentUserId: String, targetUserId: String): Boolean
    fun userConverToUserDTO(user:User): UserDTO
    suspend fun editProfile(user: User)
    suspend fun createUserReport(report: Report<User>)
    suspend fun deleteAccount(userId: String)
    suspend fun checkUser(userId: String): Boolean
}