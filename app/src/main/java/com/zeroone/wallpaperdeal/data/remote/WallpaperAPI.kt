package com.zeroone.wallpaperdeal.data.remote

import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.response.ResponseWallpaper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WallpaperAPI {
    @POST("/api/1.0/wallpapers")
    suspend fun saveWallpaper(@Body wallpaper: Wallpaper)

    @GET("/api/1.0/wallpapers")
    suspend fun getWallpapers() : ResponseWallpaper

    @GET("/api/1.0/wallpapers/category/{categoryName}")
    suspend fun getWallpapersByCategory(@Path("categoryName") categoryName: String) : ResponseWallpaper

    @GET("/api/1.0/wallpapers/owner/{ownerId}")
    suspend fun getWallpapersByOwner(@Path("ownerId") ownerId: String) : ResponseWallpaper
}