package com.zeroone.wallpaperdeal.data.model

import com.google.gson.annotations.SerializedName
import com.zeroone.wallpaperdeal.data.model.Wallpaper

data class ResponseWallpaper(
    val response : String,
    val payload : List<Wallpaper>
)