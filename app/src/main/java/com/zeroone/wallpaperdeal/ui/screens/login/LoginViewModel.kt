package com.zeroone.wallpaperdeal.ui.screens.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.datatransport.cct.internal.LogEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.ui.screens.login.authgoogle.SignInResult
import com.zeroone.wallpaperdeal.ui.screens.login.authgoogle.SignInState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOError
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val auth:FirebaseAuth,
    private val userRepository: UserRepository
) : ViewModel(){

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()
    var checkUserState: MutableState<Boolean> = mutableStateOf(false)

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }
    suspend fun saveUserToDb(user: User){
        checkUser(userId = user.userId)
        var orgUser = userRepository.getUser(userId = user.userId)
        Log.e("saveuserDatabae-orgUser-check", orgUser.toString())
        Log.e("checkUserViemodel", checkUserState.value.toString())
        if (checkUserState.value){
           try {
               FirebaseMessaging.getInstance().token.addOnCompleteListener{ taskToken ->
                   if(taskToken.isSuccessful){
                       val token = taskToken.result.toString()
                       Log.e("Create Fcm Token ", taskToken.result.toString())
                       fcmTokenSaveDb(newToken = token)
                   }else{
                       Log.e("Create Fcm Token ", "Fail")
                   }
               }
           }catch (e: IOError){
               Log.e("saveUserToDb", e.message.toString())
           }
        }else{
            try {
                Log.e("saveuserDatabae-check", user.toString())
                userRepository.saveUser(user)
                FirebaseMessaging.getInstance().token.addOnCompleteListener{ taskToken ->
                    if(taskToken.isSuccessful){
                        val token = taskToken.result.toString()
                        Log.e("Create Fcm Token ", taskToken.result.toString())
                        fcmTokenSaveDb(newToken = token)
                    }else{
                        Log.e("Create Fcm Token ", "Fail")
                    }
                }
            }catch (e: IOError){
                Log.e("saveUserToDb", e.message.toString())
            }
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
    fun login(textEmail: String, textPassword: String, loading: () -> Unit, context: Context){
        auth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener {
            if(it.isSuccessful){
                val verification = auth.currentUser!!.isEmailVerified
                if(verification){
                    loading()
                    Log.d("signInWithEmail:", "success")
                    navigateToHome(context)
                    FirebaseMessaging.getInstance().token.addOnCompleteListener{ taskToken ->
                        if(taskToken.isSuccessful){
                            val token = taskToken.result.toString()
                            Log.e("Create Fcm Token ", taskToken.result.toString())
                            fcmTokenSaveDb(newToken = token)
                        }else{
                            Log.e("Create Fcm Token ", "Fail")
                        }
                    }
                }else{
                    auth.signOut()
                    Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show()
                }

            }else{
                loading()
                Log.e("signInWithEmail:", it.exception.toString())
                Toast.makeText(context, "Check your login information", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun checkUser(userId: String) {
        checkUserState.value = userRepository.checkUser(userId = userId)
    }
    fun fcmTokenSaveDb(newToken: String){
        auth.currentUser?.let {
            CoroutineScope(Dispatchers.Main).launch{
                val user = userRepository.getUser(userId = it.uid)
                user?.fcmToken = newToken
                Log.e("User-check-tokensavedb", user.toString())
                userRepository.saveUser(user = user!!)
            }
        }
    }
}