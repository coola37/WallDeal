package com.zeroone.wallpaperdeal.data.model

data class User(
    val userId: String,
    val email: String,
    val username: String,
    val userDetail: UserDetail?
)
