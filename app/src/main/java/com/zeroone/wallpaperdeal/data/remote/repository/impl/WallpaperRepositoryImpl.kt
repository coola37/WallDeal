package com.zeroone.wallpaperdeal.data.remote.repository.impl

import android.util.Log
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.remote.WallpaperAPI
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import java.io.EOFException
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(private val api: WallpaperAPI) : WallpaperRepository {
    override suspend fun saveWallpaper(wallpaper: Wallpaper): String {
        try{
            api.saveWallpaper(wallpaper = wallpaper)
            return "Wallpaper has been saved"
        }catch (e: EOFException){
            Log.e("SaveWallpaperError:", e.message.toString())
            return "Failed"
        }
    }

    override suspend fun getWallpapers(): List<Wallpaper> {
        return api.getWallpapers()
    }

    override suspend fun getWallpapersByCategory(categoryName: String): List<Wallpaper> {
        return api.getWallpapersByCategory(categoryName = categoryName)
    }

    override suspend fun getWallpapersByOwner(ownerId: String): List<Wallpaper> {
        return api.getWallpapersByOwner(ownerId = ownerId)
    }
}