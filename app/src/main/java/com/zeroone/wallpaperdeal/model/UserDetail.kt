package com.zeroone.wallpaperdeal.model

data class UserDetail(
    val profilePhoto: String?,
    val favoriteWallpapers: List<Wallpaper?>,
    val followers: List<User?>,
    val followed: List<User?>
)
