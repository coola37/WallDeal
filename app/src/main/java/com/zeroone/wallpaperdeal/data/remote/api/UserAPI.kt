package com.zeroone.wallpaperdeal.data.remote.api

import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserAPI {
    @POST("/api/1.0/users")
    suspend fun saveUser(@Body user: User)
    @GET("/api/1.0/users/{userId}")
    suspend fun getUser(@Path("userId") userId: String) : User
    @GET("/api/1.0/users/check-user/{userId}")
    suspend fun checkUser(@Path("userId") userId: String) : Boolean
    @GET("/api/1.0/users")
    suspend fun getUsers() : List<User>
    @GET("/api/1.0/users/check/favorites/{userId}/{wallpaperId}")
    suspend fun checkFavorites(@Path("userId") userId: String, @Path("wallpaperId") wallpaperId: String) : Boolean
    @PUT("/api/1.0/users/follow-or-unfollow/{currentUserId}/{targetUserId}")
    suspend fun followOrUnfollow(@Path("currentUserId") currentUserId: String, @Path("targetUserId") targetUserId: String)
    @GET("/api/1.0/users/check-follow/{currentUserId}/{targetUserId}")
    suspend fun checkFollow(@Path("currentUserId") currentUserId: String, @Path("targetUserId") targetUserId: String): Boolean
    @PUT("/api/1.0/users/edit-profile")
    suspend fun editProfile(@Body user: User)
    @POST("/api/1.0/reports")
    suspend fun createUserReport(@Body report: Report<User>)
    @DELETE("/api/1.0/users/delete-user/{userId}")
    suspend fun deleteAccount(@Path("userId") userId: String)
}