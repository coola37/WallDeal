package com.zeroone.wallpaperdeal.data.remote.api

import com.zeroone.wallpaperdeal.data.model.LikeRequest
import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.model.ResponseWallpaper
import com.zeroone.wallpaperdeal.data.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

import retrofit2.Response
import retrofit2.http.*

interface WallpaperAPI {
    @POST("/api/1.0/wallpapers")
    suspend fun saveWallpaper(@Header("Authorization") token:String, @Body wallpaper: Wallpaper)
    @GET("/api/1.0/wallpapers")
    suspend fun getWallpapers(@Header("Authorization") token:String) : ResponseWallpaper

    @GET("/api/1.0/wallpapers/get/{wallpaperId}")
    suspend fun getWallpaperById(@Header("Authorization") token:String, @Path("wallpaperId" ) wallpaperId: Int) : Wallpaper

    @GET("/api/1.0/wallpapers/category/{categoryName}")
    suspend fun getWallpapersByCategory(@Header("Authorization") token:String, @Path("categoryName") categoryName: String) : ResponseWallpaper

    @GET("/api/1.0/wallpapers/owner/{ownerId}")
    suspend fun getWallpapersByOwner(@Header("Authorization") token:String, @Path("ownerId") ownerId: String) : ResponseWallpaper

    @PUT("/api/1.0/wallpapers/like/{wallpaperId}")
    suspend fun likeOrDislike(@Header("Authorization") token:String, @Path("wallpaperId") wallpaperId: Int, @Body likeRequest: LikeRequest)

    @GET("/api/1.0/wallpapers/like/control/{wallpaperId}/{currentUserId}")
    suspend fun checkLike(@Header("Authorization") token:String, @Path("wallpaperId") wallpaperId: Int, @Path("currentUserId") currentUserId: String) : Boolean

    @PUT("/api/1.0/wallpapers/favorite/{userId}/{wallpaperId}")
    suspend fun addFavorite(@Header("Authorization") token:String, @Path("userId") userId: String, @Path("wallpaperId") wallpaperId: Int)

    @GET("/api/1.0/wallpapers/get-wallpapers-by-followed/{currentUserId}")
    suspend fun getWallpaperByFollowed(@Header("Authorization") token:String, @Path("currentUserId") currentUserId: String) : ResponseWallpaper

    @DELETE("/api/1.0/wallpapers/delete/{wallpaperId}")
    suspend fun removeWallpaper(@Header("Authorization") token:String, @Path("wallpaperId") wallpaperId: Int)

    @GET("/api/1.0/wallpapers/get-favorite-wallpaper/{userId}")
    suspend fun getFavorites(@Header("Authorization") token:String, @Path("userId") userId: String): List<Wallpaper>
    @POST("/api/1.0/reports")
    suspend fun createWallpaperReport(@Header("Authorization") token:String, @Body report: Report<Wallpaper>)
}