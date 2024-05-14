package com.zeroone.wallpaperdeal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers")
data class Wallpaper(
    @PrimaryKey
    @ColumnInfo(name = "wallpaper_id")
    val wallpaperId: String,

    @ColumnInfo(name = "owner")
    val owner: UserDTO?,

    @ColumnInfo(name = "user_added_favorite")
    val userAddedFavorite: List<String>?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "liked_users")
    val likedUsers: List<String>?,

    @ColumnInfo(name = "like_count")
    val likeCount: Int?
)