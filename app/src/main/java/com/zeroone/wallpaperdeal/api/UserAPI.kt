package com.zeroone.wallpaperdeal.api

import com.zeroone.wallpaperdeal.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPI {
    @POST("/api/1.0/users")
    suspend fun saveUser(@Body user: User)

    @GET("/api/1.0/users/{userId}")
    suspend fun getUser(@Path("userId") userId: String) : User

    @GET("/api/1.0/users")
    suspend fun getUsers() : List<User>

    @GET("/api/1.0/users/check/favorites/{userId}/{wallpaperId}")
    suspend fun checkFavorites(@Path("userId") userId: String, @Path("wallpaperId") wallpaperId: String) : Boolean
}