package com.zeroone.wallpaperdeal.data.remote.repository.impl

import android.util.Log
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.remote.UserAPI
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import java.io.EOFException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: UserAPI): UserRepository{
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
            return null
        }
    }
}