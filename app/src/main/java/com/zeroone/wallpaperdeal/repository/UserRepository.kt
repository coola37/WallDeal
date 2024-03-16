package com.zeroone.wallpaperdeal.repository

import com.zeroone.wallpaperdeal.model.User


interface UserRepository {
    suspend fun saveUser(user: User): String
    suspend fun getUser(userId: String): User?

    suspend fun getUsers(): List<User>
}