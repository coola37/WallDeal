package com.zeroone.wallpaperdeal.ui.screens.share

import com.zeroone.wallpaperdeal.data.model.Wallpaper

data class WallpaperState(
    val isLoading: Boolean =  false,
    val wallpapers: List<Wallpaper> = emptyList(),
    val error: String = ""
)
