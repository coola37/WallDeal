package com.zeroone.wallpaperdeal.model

data class User(
    var userId: String,
    var email: String,
    var username: String,
    var wallDealId: String?,
    var userDetail: UserDetail?,
    var fcmToken: String?
)
