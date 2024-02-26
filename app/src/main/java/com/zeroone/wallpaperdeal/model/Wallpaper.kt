package com.zeroone.wallpaperdeal.model

data class Wallpaper(
    val wallpaperId: String,
    val name: String,
    val owner: String,
    val category: Category,
    val blurHash: String
)
