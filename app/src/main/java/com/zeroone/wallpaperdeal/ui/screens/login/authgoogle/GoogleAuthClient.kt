package com.zeroone.wallpaperdeal.ui.screens.login.authgoogle

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.model.User
import com.zeroone.wallpaperdeal.model.UserDetail
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class GoogleAuthClient @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val auth: FirebaseAuth
) {

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val googleUser = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = googleUser?.run {
                    googleUser?.run {
                        User(
                            userId = googleUser.uid,
                            email = googleUser.email!!,
                            username = googleUser.displayName!!,
                            userDetail = null,
                            fcmToken = "",
                            wallDealId = ""
                        )
                    }},
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): User? = auth.currentUser?.run {
        User(
            userId = uid,
            email = email!!,
            username = displayName!!,
            userDetail = UserDetail(
                profilePhoto = photoUrl.toString(),
                followed = emptyList(),
                followers = emptyList(),
                favoriteWallpapers = emptyList()
            ),
            fcmToken = "",
            wallDealId = ""
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}