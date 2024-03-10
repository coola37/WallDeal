package com.zeroone.wallpaperdeal.data.remote.repository

import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.response.ResponseWallpaper
import retrofit2.http.Body

import retrofit2.http.Path

interface WallpaperRepository {
    suspend fun saveWallpaper(wallpaper: Wallpaper) : String

    suspend fun getWallpapers() : ResponseWallpaper

    suspend fun getWallpaperById(@Path("wallpaperId") wallpaperId: String) : Wallpaper

    suspend fun getWallpapersByCategory(@Path("categoryName") categoryName: String) : ResponseWallpaper

    suspend fun getWallpapersByOwner(@Path("ownerId") ownerId: String) : ResponseWallpaper
}