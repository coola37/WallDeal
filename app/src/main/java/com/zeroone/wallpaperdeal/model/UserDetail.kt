package com.zeroone.wallpaperdeal.model

data class UserDetail(
    var profilePhoto: String?,
    var favoriteWallpapers: List<Wallpaper?>,
    var followers: List<UserDTO?>,
    var followed: List<UserDTO?>
)
