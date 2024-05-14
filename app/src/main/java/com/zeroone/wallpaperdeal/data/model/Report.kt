package com.zeroone.wallpaperdeal.data.model

data class Report<T>(
    var reportId: String,
    var message: String,
    var payload: T
)
