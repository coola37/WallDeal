package com.zeroone.wallpaperdeal.data.remote.repository

import com.zeroone.wallpaperdeal.data.model.LikeRequest
import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.model.ResponseWallpaper

interface WallpaperRepository {
    suspend fun saveWallpaper(wallpaper: Wallpaper) : String
    suspend fun getWallpapers() : ResponseWallpaper
    suspend fun getWallpaperById(wallpaperId: String) : Wallpaper
    suspend fun getWallpapersByCategory(categoryName: String) : ResponseWallpaper
    suspend fun getWallpapersByOwner(ownerId: String) : ResponseWallpaper
    suspend fun likeOrDislike(wallpaperId: String, likeRequest: LikeRequest)
    suspend fun addFavorite(userId: String, wallpaperId: String)
    suspend fun checkLike(wallpaperId: String, currentUserId: String) : Boolean
    suspend fun getWallpaperByFollowed(currentUserId: String) : ResponseWallpaper
    suspend fun removeWallpaper(wallpaperId: String)
    suspend fun createWallpaperReport(report: Report<Wallpaper>)

}