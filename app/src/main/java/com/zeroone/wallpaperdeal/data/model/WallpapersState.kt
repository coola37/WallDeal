package com.zeroone.wallpaperdeal.data.model

import com.zeroone.wallpaperdeal.data.model.Wallpaper

data class WallpapersState(
    val isLoading: Boolean =  false,
    val wallpapers: List<Wallpaper> = emptyList(),
    val error: String = ""
)
