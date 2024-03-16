package com.zeroone.wallpaperdeal.repository

import com.zeroone.wallpaperdeal.model.LikeRequest
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.model.ResponseWallpaper
import retrofit2.http.Body

import retrofit2.http.Path

interface WallpaperRepository {
    suspend fun saveWallpaper(wallpaper: Wallpaper) : String

    suspend fun getWallpapers() : ResponseWallpaper

    suspend fun getWallpaperById(@Path("wallpaperId") wallpaperId: String) : Wallpaper

    suspend fun getWallpapersByCategory(@Path("categoryName") categoryName: String) : ResponseWallpaper

    suspend fun getWallpapersByOwner(@Path("ownerId") ownerId: String) : ResponseWallpaper

    suspend fun likeOrDislike(@Path("wallpaperId") wallpaperId: String, @Body likeRequest: LikeRequest)


    suspend fun checkLike(@Path("wallpaperId") wallpaperId: String, @Path("currentUserId") currentUserId: String) : Boolean
}