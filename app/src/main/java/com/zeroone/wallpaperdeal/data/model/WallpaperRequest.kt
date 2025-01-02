package com.zeroone.wallpaperdeal.data.model

data class WallpaperRequest(
    val wallpaperRequestId: String,
    val message: String,
    val senderUser: User,
    val receiverUser: User,
    val imageUrl: String
)
