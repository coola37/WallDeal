package com.zeroone.wallpaperdeal.data.remote.repository

import com.zeroone.wallpaperdeal.data.model.LikeRequest
import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.model.ResponseWallpaper
import retrofit2.http.Body

interface WallpaperRepository {
    suspend fun saveWallpaper(token:String, wallpaper: Wallpaper) : String
    suspend fun getWallpapers(token:String ) : ResponseWallpaper
    suspend fun getWallpaperById(token:String, wallpaperId: Int) : Wallpaper
    suspend fun getWallpapersByCategory(token:String, categoryName: String) : ResponseWallpaper
    suspend fun getWallpapersByOwner(token:String, ownerId: String) : ResponseWallpaper
    suspend fun likeOrDislike(token:String, wallpaperId: Int, likeRequest: LikeRequest)
    suspend fun addFavorite(token:String, userId: String, wallpaperId: Int)
    suspend fun checkLike(token:String, wallpaperId: Int, currentUserId: String) : Boolean
    suspend fun getWallpaperByFollowed(token:String, currentUserId: String) : ResponseWallpaper
    suspend fun removeWallpaper(token:String, wallpaperId: Int)
    suspend fun createWallpaperReport(token:String, report: Report<Wallpaper>)
    suspend fun getFavorites(token:String, userId: String): List<Wallpaper>
}
