package com.zeroone.wallpaperdeal.data.remote

import com.zeroone.wallpaperdeal.data.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPI {
    @POST("/api/1.0/users")
    suspend fun saveUser(@Body user: User)

    @GET("/api/1.0/users/{userId}")
    suspend fun getUser(@Path("userId") userId: String) : User
}