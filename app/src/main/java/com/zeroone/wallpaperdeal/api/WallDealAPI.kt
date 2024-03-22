package com.zeroone.wallpaperdeal.api

import com.zeroone.wallpaperdeal.model.Wallpaper
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WallDealAPI {
    @POST("/api/1.0/walldeal/send-request/{senderUser}/{receiverUser}")
    suspend fun sendWallDealRequest(@Path("senderUser") senderUser: String, @Path("receiverUser") receiverUser: String)

    @DELETE("/api/1.0/walldeal/delete-request/{requestId}")
    suspend fun deleteRequest(@Path("requestId") requestId: String) : String

    @GET("/api/1.0/walldeal/check-walldeal-request/{currentUserId}/{targetUserId}")
    suspend fun checkWallDealRequest(@Path("currentUserId") currentUserId: String, @Path("targetUserId") targetUserId: String) : Boolean

    @GET("/api/1.0/walldeal/check-walldeal/{targetUserId}")
    suspend fun checkWalldeal(@Path("targetUserId") targetUserId: String) : Boolean

    @GET("/api/1.0/walldeal/check-walldeal-for-between-user-to-user/{currentUserId}/{targetUserId}")
    suspend fun checkWalldealForBetweenUserToUser(@Path("currentUserId") currentUserId: String, @Path("targetUserId") targetUserId: String) : Boolean

    @PUT("/api/1.0/walldeal/send-post/{currentUserId}")
    suspend fun sendPost(@Path("currentUserId") currentUserId: String, @Body post: Wallpaper)
}