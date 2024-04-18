package com.zeroone.wallpaperdeal.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.net.URL

fun downloadWallpaper(context: Context, imageUrl: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // For Android 10 (API 29) and above, use the MediaStore API
        downloadWallpaperForQ(context, imageUrl)
    } else {
        // For Android versions below 10, use the DownloadManager
        downloadWallpaperForLegacy(context, imageUrl)
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun downloadWallpaperForQ(context: Context, imageUrl: String) {
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
private fun downloadWallpaperForLegacy(context: Context, imageUrl: String) {
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