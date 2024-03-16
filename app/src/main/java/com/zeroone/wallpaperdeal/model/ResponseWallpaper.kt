package com.zeroone.wallpaperdeal.model

import com.google.gson.annotations.SerializedName
import com.zeroone.wallpaperdeal.model.Wallpaper

data class ResponseWallpaper(
    val response : String,
    val payload : List<Wallpaper>
)