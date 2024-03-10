package com.zeroone.wallpaperdeal.data.model

data class Wallpaper(
    val wallpaperId: String,
    val description: String?,
    val owner: User?,
    val imageUrl: String,
    val category: String,
    val gradiantUrl: String?,
    val likedUsers: List<String>?,
    val likeCount: Int?
)
