package com.zeroone.wallpaperdeal.ui.screens.login.authgoogle

import com.zeroone.wallpaperdeal.model.User

data class SignInResult(
    val data: User?,
    val errorMessage: String?
)