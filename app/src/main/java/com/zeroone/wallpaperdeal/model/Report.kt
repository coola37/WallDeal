package com.zeroone.wallpaperdeal.model

data class Report<T>(
    var reportId: String,
    var message: String,
    var payload: T
)
