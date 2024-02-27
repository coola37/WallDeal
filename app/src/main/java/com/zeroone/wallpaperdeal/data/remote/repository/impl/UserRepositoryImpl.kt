package com.zeroone.wallpaperdeal.data.remote.repository.impl

import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.remote.UserAPI
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: UserAPI): UserRepository{
    override suspend fun saveUser(user: User): String {
        api.saveUser(user)
        return "User has been created"
    }
}