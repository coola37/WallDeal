package com.zeroone.wallpaperdeal.ui.screens

import android.graphics.Bitmap

interface ScreenCallback {
    fun onSetWallpaperClick(bitmap: Bitmap)
}