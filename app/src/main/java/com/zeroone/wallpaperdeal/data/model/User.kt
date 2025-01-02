package com.zeroone.wallpaperdeal.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @ColumnInfo("user_id")
    var userId: String,
    @ColumnInfo("email")
    var email: String,
    @ColumnInfo("username")
    var username: String,
    @ColumnInfo("couple_id")
    var coupleId: String?,
    @ColumnInfo("profile_photo")
    var profilePhoto: String?,
    @ColumnInfo("fcm_token")
    var fcmToken: String?,
    @ColumnInfo("favorite_wallpapers")
    var favoriteWallpapers: Set<Int>?,
    @ColumnInfo("added_favorites")
    var addedFavorites: Set<Int>?,
    @ColumnInfo("followers")
    var followers: Set<String>?,
    @ColumnInfo("followed")
    var followed: Set<String>?,
)
