package com.zeroone.wallpaperdeal.model

data class WallpaperRequest(
    val message: String,
    val senderUser: User,
    val receiverUsers: List<User>,
    val imageUrl: String
)
