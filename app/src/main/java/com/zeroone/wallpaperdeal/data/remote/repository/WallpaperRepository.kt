package com.zeroone.wallpaperdeal.data.remote.repository

import com.zeroone.wallpaperdeal.data.model.Wallpaper
import retrofit2.http.Body

import retrofit2.http.Path

interface WallpaperRepository {
    suspend fun saveWallpaper(wallpaper: Wallpaper) : String

    suspend fun getWallpapers() : List<Wallpaper>

    suspend fun getWallpapersByCategory(@Path("categoryName") categoryName: String) : List<Wallpaper>

    suspend fun getWallpapersByOwner(@Path("ownerId") ownerId: String) : List<Wallpaper>
}