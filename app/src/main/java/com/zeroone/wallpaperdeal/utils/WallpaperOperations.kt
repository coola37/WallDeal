package com.zeroone.wallpaperdeal.utils

import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import java.io.IOException

fun downloadWallpaper(context: Context, imageUrl: String) {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val uri = Uri.parse(imageUrl)

    val request = DownloadManager.Request(uri).apply {
        setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        setTitle("Downloading Wallpaper")
        setDescription("Downloading wallpaper image")
        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "walldeal_${System.currentTimeMillis()}.jpg")
    }

    downloadManager.enqueue(request)
}
fun setWallpaper(context: Context, bitmap: Bitmap) {
    try {
        val wallpaperManager = context.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
        wallpaperManager.setBitmap(bitmap)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}