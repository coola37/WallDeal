package com.zeroone.wallpaperdeal.model

data class WallDeal(
    val groupId: String,
    var user1: UserDTO,
    var user2: UserDTO,
    var requestId: String
)
