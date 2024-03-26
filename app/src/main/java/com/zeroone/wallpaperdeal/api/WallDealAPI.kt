package com.zeroone.wallpaperdeal.api

import com.zeroone.wallpaperdeal.model.WallDeal
import com.zeroone.wallpaperdeal.model.WallDealRequest
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.model.WallpaperRequest
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
    suspend fun checkWalldealForBetweenUserToUser(@Path("currentUserId") currentUserId: String,
                                                  @Path("targetUserId") targetUserId: String) : Boolean
    @PUT("/api/1.0/walldeal/send-post/{currentUserId}")
    suspend fun sendPost(@Path("currentUserId") currentUserId: String, @Body request: WallpaperRequest)
    @PUT("/api/1.0/walldeal/cancel-post/{currentUserId}")
    suspend fun cancelPost(@Path("currentUserId") currentUserId: String, @Body request: WallpaperRequest)
    @GET("/api/1.0/walldeal/request-notification-check/{userId}}")
    suspend fun checkWallDealRequests(@Path("userId") userId: String) : Boolean
    @GET("/api/1.0/walldeal/get-request/{userId}")
    suspend fun getRequestsByUserId(@Path("userId") userId: String) : List<WallDealRequest>

    @GET("/api/1.0/walldeal/get-walldeal/{userId}")
    suspend fun getWallDeal(@Path("userId") userId: String) : WallDeal
}