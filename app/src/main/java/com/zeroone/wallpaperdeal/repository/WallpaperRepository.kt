package com.zeroone.wallpaperdeal.repository

import com.zeroone.wallpaperdeal.model.LikeRequest
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.model.ResponseWallpaper
import retrofit2.http.Body

import retrofit2.http.Path

interface WallpaperRepository {
    suspend fun saveWallpaper(wallpaper: Wallpaper) : String
    suspend fun getWallpapers() : ResponseWallpaper
    suspend fun getWallpaperById(wallpaperId: String) : Wallpaper
    suspend fun getWallpapersByCategory(categoryName: String) : ResponseWallpaper
    suspend fun getWallpapersByOwner(ownerId: String) : ResponseWallpaper
    suspend fun likeOrDislike(wallpaperId: String, likeRequest: LikeRequest)
    suspend fun addFavorite(userId: String, wallpaperId: String)
    suspend fun checkLike(wallpaperId: String, currentUserId: String) : Boolean
}