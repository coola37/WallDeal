package com.zeroone.wallpaperdeal.model

data class User(
    val userId: String,
    val email: String,
    val username: String,
    val wallDealId: String?,
    val userDetail: UserDetail?
)
