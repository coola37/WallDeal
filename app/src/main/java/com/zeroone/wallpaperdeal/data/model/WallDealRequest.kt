package com.zeroone.wallpaperdeal.data.model

data class WallDealRequest(
    val wallDealRequestId: String,
    val title: String,
    val senderUser: UserDTO,
    val receiverUser: UserDTO,
    val possibleWallDeal: WallDeal
)
