package com.zeroone.wallpaperdeal.data.remote.api

import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserAPI {
    @POST("/api/1.0/users")
    suspend fun saveUser(@Header("Authorization") token:String, @Body user: User)
    @GET("/api/1.0/users/{userId}")
    suspend fun getUser(@Header("Authorization") token:String, @Path("userId") userId: String) : User
    @GET("/api/1.0/users/check-user/{userId}")
    suspend fun checkUser(@Header("Authorization") token:String, @Path("userId") userId: String) : Boolean
    @GET("/api/1.0/users")
    suspend fun getUsers(@Header("Authorization") token:String) : Set<User>
    @GET("/api/1.0/users/check/favorites/{userId}/{wallpaperId}")
    suspend fun checkFavorites(@Header("Authorization") token:String, @Path("userId") userId: String, @Path("wallpaperId") wallpaperId: Int) : Boolean
    @PUT("/api/1.0/users/follow-or-unfollow/{currentUserId}/{targetUserId}")
    suspend fun followOrUnfollow(@Header("Authorization") token:String, @Path("currentUserId") currentUserId: String, @Path("targetUserId") targetUserId: String)
    @GET("/api/1.0/users/check-follow/{currentUserId}/{targetUserId}")
    suspend fun checkFollow(@Header("Authorization") token:String, @Path("currentUserId") currentUserId: String, @Path("targetUserId") targetUserId: String): Boolean
    @PUT("/api/1.0/users/edit-profile")
    suspend fun editProfile(@Header("Authorization") token:String, @Body user: User)
    @POST("/api/1.0/reports")
    suspend fun createUserReport(@Header("Authorization") token:String, @Body report: Report<User>)
    @DELETE("/api/1.0/users/delete-user/{userId}")
    suspend fun deleteAccount(@Header("Authorization") token:String, @Path("userId") userId: String)
    @GET("/api/1.0/users/username-check/{username}")
    suspend fun checkUsedUsername(@Path("username") username:String): Boolean
}