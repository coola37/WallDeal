package com.zeroone.wallpaperdeal.repository.impl

import android.util.Log
import com.zeroone.wallpaperdeal.model.LikeRequest
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.api.WallpaperAPI
import com.zeroone.wallpaperdeal.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.model.ResponseWallpaper
import java.io.EOFException
import javax.inject.Inject
import kotlin.RuntimeException

class WallpaperRepositoryImpl @Inject constructor(private val api: WallpaperAPI) :
    WallpaperRepository {
    override suspend fun saveWallpaper(wallpaper: Wallpaper): String {
        return try{
            api.saveWallpaper(wallpaper = wallpaper)
            "Wallpaper has been saved"
        }catch (e: EOFException){
            Log.e("SaveWallpaperError:", e.message.toString())
            "Failed"
        }
    }

    override suspend fun getWallpapers(): ResponseWallpaper {
        return api.getWallpapers()
    }

    override suspend fun getWallpaperById(wallpaperId: String): Wallpaper {
        return api.getWallpaperById(wallpaperId = wallpaperId)
    }

    override suspend fun getWallpapersByCategory(categoryName: String): ResponseWallpaper {
        return api.getWallpapersByCategory(categoryName = categoryName)
    }

    override suspend fun getWallpapersByOwner(ownerId: String): ResponseWallpaper {
        return api.getWallpapersByOwner(ownerId = ownerId)
    }

    override suspend fun likeOrDislike(wallpaperId: String, likeRequest: LikeRequest) {
        api.likeOrDislike(wallpaperId = wallpaperId, likeRequest = likeRequest)
    }

    override suspend fun addFavorite(userId: String, wallpaperId: String) {
        api.addFavorite(userId = userId, wallpaperId = wallpaperId)
    }


    override suspend fun checkLike(wallpaperId: String, currentUserId: String): Boolean {
        return api.checkLike(wallpaperId = wallpaperId, currentUserId = currentUserId)
    }

}