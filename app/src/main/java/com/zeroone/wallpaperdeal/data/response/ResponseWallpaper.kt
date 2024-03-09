package com.zeroone.wallpaperdeal.data.response

import com.google.gson.annotations.SerializedName
import com.zeroone.wallpaperdeal.data.model.Wallpaper

data class ResponseWallpaper(
    val response : String,
    val payload : List<Wallpaper>
)