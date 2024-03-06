package com.zeroone.wallpaperdeal.data.remote.repository

import com.zeroone.wallpaperdeal.data.model.User


interface UserRepository {
    suspend fun saveUser(user: User): String
    suspend fun getUser(userId: String): User?
}