package com.zeroone.wallpaperdeal.ui.screens.register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOError
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: UserRepository,
    ) : ViewModel() {

        suspend fun saveUserToDb(user: User){
            try {
                repository.saveUser(user)
            }catch (e: IOError){
                Log.e("saveUserToDb", e.message.toString())
            }
        }
}