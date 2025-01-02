package com.zeroone.wallpaperdeal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallpapers")
data class Wallpaper(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallpaper_id")
    val wallpaperId: Int = 0,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "owner")
    val user: User?,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "like_count")
    val likeCount: Int?,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "liked_users")
    val likedUsers: Set<String>?,

    @ColumnInfo(name = "user_added_favorite")
    val userAddedFavorite: Set<String>?,
)