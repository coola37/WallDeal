package com.zeroone.wallpaperdeal.data.model

data class UserDTO(
    val userId: String,
    val email: String,
    val username: String,
    val wallDealId: String?,
    val profilePhoto: String?,
    var fcmToken: String?
)
