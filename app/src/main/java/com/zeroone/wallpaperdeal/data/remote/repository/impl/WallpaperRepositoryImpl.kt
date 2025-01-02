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
    override suspend fun saveWallpaper(token:String, wallpaper: Wallpaper): String {
        return try{
            val bearer = "Bearer $token"
            api.saveWallpaper(token = bearer, wallpaper = wallpaper)
            Log.e("saveWallapaperToken", bearer)
            "Wallpaper has been saved"
        }catch (e: EOFException){
            Log.e("SaveWallpaperError:", e.message.toString())
            "Failed"
        }
    }

    override suspend fun getWallpapers(token:String): ResponseWallpaper {
        val bearer = "Bearer $token"
        try{ return api.getWallpapers(token = bearer)
        }catch (e: IOException){
            throw e
        }
    }

    override suspend fun getWallpaperById(token:String, wallpaperId: Int): Wallpaper {
        val bearer = "Bearer $token"
        return api.getWallpaperById(token = bearer,wallpaperId = wallpaperId)
    }

    override suspend fun getWallpapersByCategory(token:String, categoryName: String): ResponseWallpaper {
        val bearer = "Bearer $token"
        return api.getWallpapersByCategory(token = bearer,categoryName = categoryName)
    }

    override suspend fun getWallpapersByOwner(token:String, ownerId: String): ResponseWallpaper {
        val bearer = "Bearer $token"
        return api.getWallpapersByOwner(token = bearer,ownerId = ownerId)
    }

    override suspend fun likeOrDislike(token:String, wallpaperId: Int, likeRequest: LikeRequest) {
        val bearer = "Bearer $token"
        api.likeOrDislike(token = bearer,wallpaperId = wallpaperId, likeRequest = likeRequest)
    }

    override suspend fun addFavorite(token:String, userId: String, wallpaperId: Int) {
        val bearer = "Bearer $token"
        api.addFavorite(token = bearer,userId = userId, wallpaperId = wallpaperId)
    }


    override suspend fun checkLike(token:String, wallpaperId: Int, currentUserId: String): Boolean {
        val bearer = "Bearer $token"
        return api.checkLike(token = bearer,wallpaperId = wallpaperId, currentUserId = currentUserId)
    }

    override suspend fun getWallpaperByFollowed(token:String, currentUserId: String) : ResponseWallpaper {
        val bearer = "Bearer $token"
        return api.getWallpaperByFollowed(token = bearer,currentUserId = currentUserId)
    }

    override suspend fun removeWallpaper(token:String, wallpaperId: Int) {
        val bearer = "Bearer $token"
        try{ api.removeWallpaper(token = bearer,wallpaperId = wallpaperId) }
        catch (ex: RuntimeException){ throw ex}
    }

    override suspend fun createWallpaperReport(token:String, report: Report<Wallpaper>) {
        val bearer = "Bearer $token"
        try {
            api.createWallpaperReport(token = bearer,report = report)
        }catch (e: RuntimeException){
            throw e
        }
    }

    override suspend fun getFavorites(token:String, userId: String): List<Wallpaper> {
        val bearer = "Bearer $token"
        return api.getFavorites(token = bearer,userId = userId)
    }
}
