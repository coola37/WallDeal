package com.zeroone.wallpaperdeal.data.model

data class CoupleRequest(
    val coupleRequestId: String,
    val title: String,
    val senderUser: User,
    val receiverUser: User,
)
