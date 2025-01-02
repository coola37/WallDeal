package com.zeroone.wallpaperdeal.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.screens.register.sendVerificationEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService(){

    private lateinit var auth: FirebaseAuth
    @Inject
    lateinit var userRepository: UserRepository
    override fun onMessageReceived(message: RemoteMessage) {
        var notificationTitle: String = message.notification?.title!!
        var notificationBody: String = message.notification?.body!!
        var notificationData: Map<String, String> = message.data

        Log.e("FCM", "Title: $notificationTitle body: $notificationBody data: $notificationData")
    }

    override fun onNewToken(token: String) {
        var newToken = token
        newTokenSaveDb(newToken = token)
    }

    private fun newTokenSaveDb(newToken: String) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            CoroutineScope(Dispatchers.IO).launch{
                it.getIdToken(true).addOnCompleteListener { taskToken ->
                    if (taskToken.isSuccessful) {
                        val token = taskToken.result?.token
                        token?.let { idToken ->
                            CoroutineScope(Dispatchers.IO).launch{
                                Log.e("Create Firebase Token", idToken)
                                val user = userRepository.getUser(userId = it.uid, token = idToken)
                                user?.fcmToken = newToken
                                userRepository.saveUser(user = user!!, token = idToken)
                            }
                        }
                    } else {
                        Log.e("Create Firebase Token", "Fail")
                    }
                }

            }
        }
    }
}