package com.zeroone.wallpaperdeal.model

data class WallDealRequest(
    val wallDealRequestId: String,
    val title: String,
    val senderUser: User,
    val receiverUser: User,
    val possibleWallDeal: WallDeal
)
