package com.zeroone.wallpaperdeal.model

data class WallpaperRequest(
    val wallpaperRequestId: String,
    val message: String,
    val senderUser: UserDTO,
    val receiverUser: UserDTO,
    val imageUrl: String
)
