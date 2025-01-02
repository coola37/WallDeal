package com.zeroone.wallpaperdeal.ui.screens.login.authgoogle

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import java.util.Collections
import javax.inject.Inject

class GoogleAuthClient @Inject constructor(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val auth: FirebaseAuth,
    private val userRepository: UserRepository
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
            val authResult = auth.signInWithCredential(googleCredentials).await()
            val googleUser = authResult.user
            val idToken = authResult.user?.getIdToken(false)?.await()?.token
            Log.e("IDToken signInWithIntent", idToken.toString())
            SignInResult(
                data = googleUser?.run {
                    googleUser?.run {
                        User(
                            userId = googleUser.uid,
                            email = googleUser.email!!,
                            username = googleUser.displayName!!,
                            fcmToken = "",
                            coupleId = "",
                            favoriteWallpapers = emptySet(),
                            addedFavorites = emptySet(),
                            followers = emptySet(),
                            followed = emptySet(),
                            profilePhoto = ""
                        )
                    }},
                errorMessage = null,
                idToken = idToken
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message,
                idToken = null
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
            fcmToken = "",
            coupleId = "",
            favoriteWallpapers = Collections.emptySet(),
            addedFavorites = Collections.emptySet(),
            followers = Collections.emptySet(),
            followed = Collections.emptySet(),
            profilePhoto = photoUrl.toString()
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