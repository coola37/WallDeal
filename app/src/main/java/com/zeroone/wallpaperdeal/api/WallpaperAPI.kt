package com.zeroone.wallpaperdeal.api

import com.zeroone.wallpaperdeal.model.LikeRequest
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.model.ResponseWallpaper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WallpaperAPI {
    @POST("/api/1.0/wallpapers")
    suspend fun saveWallpaper(@Body wallpaper: Wallpaper)

    @GET("/api/1.0/wallpapers")
    suspend fun getWallpapers() : ResponseWallpaper

    @GET("/api/1.0/wallpapers/get/{wallpaperId}")
    suspend fun getWallpaperById(@Path("wallpaperId" ) wallpaperId: String) : Wallpaper

    @GET("/api/1.0/wallpapers/category/{categoryName}")
    suspend fun getWallpapersByCategory(@Path("categoryName") categoryName: String) : ResponseWallpaper

    @GET("/api/1.0/wallpapers/owner/{ownerId}")
    suspend fun getWallpapersByOwner(@Path("ownerId") ownerId: String) : ResponseWallpaper

    @PUT("/api/1.0/wallpapers/like/{wallpaperId}")
    suspend fun likeOrDislike(@Path("wallpaperId") wallpaperId: String, @Body likeRequest: LikeRequest)

    @GET("/api/1.0/wallpapers/like/control/{wallpaperId}/{currentUserId}")
    suspend fun checkLike(@Path("wallpaperId") wallpaperId: String, @Path("currentUserId") currentUserId: String) : Boolean

    @PUT("/api/1.0/wallpapers/favorite/{userId}/{wallpaperId}")
    suspend fun addFavorite(@Path("userId") userId: String, @Path("wallpaperId") wallpaperId: String)
}