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
    @ColumnInfo("walldeal_id")
    var wallDealId: String?,
    @ColumnInfo("user_detail")
    var userDetail: UserDetail?,
    @ColumnInfo("fcm_token")
    var fcmToken: String?
)
