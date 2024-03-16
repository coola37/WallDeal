package com.zeroone.wallpaperdeal.model

data class User(
    val userId: String,
    val email: String,
    val username: String,
    val profilePhoto: String?,
    val wallDealId: String?
)
