package com.zeroone.wallpaperdeal.model

import com.zeroone.wallpaperdeal.model.Wallpaper

data class WallpapersState(
    val isLoading: Boolean =  false,
    val wallpapers: List<Wallpaper> = emptyList(),
    val error: String = ""
)
