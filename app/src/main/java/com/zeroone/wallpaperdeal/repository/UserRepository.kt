package com.zeroone.wallpaperdeal.repository

import com.zeroone.wallpaperdeal.model.User
import retrofit2.http.Path


interface UserRepository {
    suspend fun saveUser(user: User): String
    suspend fun getUser(userId: String): User?
    suspend fun getUsers(): List<User>
    suspend fun checkFavorites(userId: String, wallpaperId: String) : Boolean
}