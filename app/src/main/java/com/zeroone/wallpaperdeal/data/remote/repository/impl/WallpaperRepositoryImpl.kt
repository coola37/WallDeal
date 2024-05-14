package com.zeroone.wallpaperdeal.data.remote.repository.impl

import android.util.Log
import com.zeroone.wallpaperdeal.data.model.LikeRequest
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.remote.api.WallpaperAPI
import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import com.zeroone.wallpaperdeal.data.model.ResponseWallpaper
import java.io.EOFException
import java.io.IOException
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
        try{ return api.getWallpapers()
        }catch (e: IOException){
            throw e
        }
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

    override suspend fun getWallpaperByFollowed(currentUserId: String) : ResponseWallpaper {
        return api.getWallpaperByFollowed(currentUserId = currentUserId)
    }

    override suspend fun removeWallpaper(wallpaperId: String) {
        try{ api.removeWallpaper(wallpaperId = wallpaperId) }
        catch (ex: RuntimeException){ throw ex}
    }

    override suspend fun createWallpaperReport(report: Report<Wallpaper>) {
        try {
            api.createWallpaperReport(report = report)
        }catch (e: RuntimeException){
            throw e
        }
    }

}