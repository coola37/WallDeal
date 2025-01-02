package com.zeroone.wallpaperdeal.ui.screens.register

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.ui.screens.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOError
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val repository: UserRepository,

    ) : ViewModel() {

        val checkUsername: MutableState<Boolean> = mutableStateOf(false)
        suspend fun checkUsingUsername(username: String) : Boolean{
            return repository.checkUsingUsername(username = username)
        }
        suspend fun saveUserToDb(textEmail: String, textPassword:String, textUsername: String,
                                 finallyOpr: () -> Unit,
                                 signInFailureOpr: () -> Unit,
                                 context: Context,
                                 usernameUsedOpr: () -> Unit
                                 ){
            try {
                val isUsing = checkUsingUsername(username = textUsername)
                if(isUsing){
                    usernameUsedOpr()
                }else{
                    auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener {
                        if(it.isSuccessful){
                            auth.currentUser?.let {
                                sendVerificationEmail(it)
                                var fcmToken = ""
                                FirebaseMessaging.getInstance().token.addOnCompleteListener { taskToken ->
                                    if (taskToken.isSuccessful) {
                                        fcmToken = taskToken.result.toString()
                                        Log.e("FCM token RegisterScreen", fcmToken)
                                    } else {
                                        Log.e("Create Fcm Token ", "Fail")
                                    }
                                }
                                val user = User(
                                    userId = it.uid,
                                    email = textEmail,
                                    username = textUsername,
                                    coupleId = "",
                                    fcmToken = fcmToken,
                                    favoriteWallpapers = Collections.emptySet(),
                                    addedFavorites = Collections.emptySet(),
                                    followers = Collections.emptySet(),
                                    followed = Collections.emptySet(),
                                    profilePhoto = ""
                                )
                                it.getIdToken(true)?.addOnCompleteListener { taskToken ->
                                    if (taskToken.isSuccessful) {
                                        val token = taskToken.result?.token
                                        token?.let { idToken ->
                                            Log.e("Create Firebase Token", token)
                                            try {
                                                CoroutineScope(Dispatchers.Main).launch{
                                                    repository.saveUser(user = user, token = idToken)
                                                }
                                            }finally {
                                                auth.signOut()
                                                finallyOpr()
                                            }
                                        }
                                    } else {
                                        Log.e("Create Firebase Token", "Fail")
                                    }
                                }

                            }
                        }else{
                            signInFailureOpr()
                            Toast.makeText(context,it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                            Log.d("UserAuth:", "failure: ", it.exception)
                        }
                    }
                }

            }catch (e: IOError){
                Log.e("saveUserToDb", e.message.toString())
            }
        }
    fun sendVerificationEmail(user: FirebaseUser){
        user.sendEmailVerification().addOnCompleteListener {
            Log.d("VerifyEmail", "sent")
        }.addOnFailureListener {
            Log.e("VerifyEmail", "did not send")
        }
    }
}