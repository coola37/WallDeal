package com.zeroone.wallpaperdeal.data.remote.api

import com.zeroone.wallpaperdeal.data.model.Couple
import com.zeroone.wallpaperdeal.data.model.CoupleRequest
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WallDealAPI {
    @POST("/api/1.0/walldeal/send-request/{senderUser}/{receiverUser}")
    suspend fun sendWallDealRequest(@Header("Authorization") token:String, @Path("senderUser") senderUser: String, @Path("receiverUser") receiverUser: String)

    @DELETE("/api/1.0/walldeal/delete-request/{requestId}")
    suspend fun deleteRequest(@Header("Authorization") token:String, @Path("requestId") requestId: String)

    @GET("/api/1.0/walldeal/check-walldeal-request/{currentUserId}/{targetUserId}")
    suspend fun checkWallDealRequest(@Header("Authorization") token:String, @Path("currentUserId") currentUserId: String, @Path("targetUserId") targetUserId: String) : Boolean

    @GET("/api/1.0/walldeal/check-walldeal/{targetUserId}")
    suspend fun checkWalldeal(@Header("Authorization") token:String, @Path("targetUserId") targetUserId: String) : Boolean
    @GET("/api/1.0/walldeal/check-walldeal-for-between-user-to-user/{currentUserId}/{targetUserId}")
    suspend fun checkWalldealForBetweenUserToUser(
        @Header("Authorization") token:String,
        @Path("currentUserId") currentUserId: String,
        @Path("targetUserId") targetUserId: String) : Boolean
    @PUT("/api/1.0/walldeal/send-post/{currentUserId}")
    suspend fun sendPost(@Header("Authorization") token:String, @Path("currentUserId") currentUserId: String, @Body request: WallpaperRequest)
    @PUT("/api/1.0/walldeal/cancel-post/{currentUserId}")
    suspend fun cancelPost(@Header("Authorization") token:String, @Path("currentUserId") currentUserId: String, @Body request: WallpaperRequest)
    @GET("/api/1.0/walldeal/request-notification-check/{userId}")
    suspend fun checkWallDealRequests(@Header("Authorization") token:String, @Path("userId") userId: String) : Boolean
    @GET("/api/1.0/walldeal/get-walldeal-request/{userId}")
    suspend fun getRequestsByUserId(@Header("Authorization") token:String, @Path("userId") userId: String) : List<CoupleRequest>
    @GET("/api/1.0/walldeal/get-walldeal/{userId}")
    suspend fun getWallDeal(@Header("Authorization") token:String, @Path("userId") userId: String) : Couple
    @POST("/api/1.0/walldeal/create-walldeal")
    suspend fun createWallDeal(@Header("Authorization") token:String, @Body couple: Couple)
    @DELETE("/api/1.0/walldeal/cancel-walldeal/{userId}")
    suspend fun cancelWallDeal(@Header("Authorization") token:String, @Path("userId") userId: String)
    @GET("/api/1.0/walldeal/get-wallpaper-request/{requestId}")
    suspend fun getWallpaperRequest(@Header("Authorization") token:String, @Path("requestId") requestId:String) : WallpaperRequest

}