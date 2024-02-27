package com.zeroone.wallpaperdeal.data.remote

import com.zeroone.wallpaperdeal.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserAPI {
    @POST("/api/1.0/users")
    suspend fun saveUser(@Body user: User)
}