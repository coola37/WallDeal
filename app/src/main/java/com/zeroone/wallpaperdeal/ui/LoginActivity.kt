package com.zeroone.wallpaperdeal.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.screens.login.LoginScreen
import com.zeroone.wallpaperdeal.ui.screens.login.LoginViewModel
import com.zeroone.wallpaperdeal.ui.screens.login.authgoogle.GoogleAuthClient
import com.zeroone.wallpaperdeal.ui.screens.login.navigateToHome
import com.zeroone.wallpaperdeal.ui.screens.register.RegisterScreen
import com.zeroone.wallpaperdeal.ui.theme.WallpaperDealTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
private const val RC_SIGN_IN = 123

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var userRepository: UserRepository

    private val googleAuthUiClient by lazy {
        GoogleAuthClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
            auth = auth,
            userRepository = userRepository
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WallpaperDealTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.LoginScreen.route ){
                        composable(Screen.LoginScreen.route){
                            val viewModel : LoginViewModel = hiltViewModel()
                            val state by viewModel.state.collectAsState()

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if(result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            val idToken = signInResult.idToken
                                            Log.e("GoogleUser", googleAuthUiClient.getSignedInUser().toString())
                                            //userRepository.saveUser(googleAuthUiClient.getSignedInUser()!!)
                                            idToken?.let {
                                                viewModel.saveUserToDb(googleAuthUiClient.getSignedInUser()!!, idToken = it)
                                            }
                                            delay(1000)
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if(state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navigateToHome(context = this@LoginActivity)
                                    viewModel.resetState()
                                }
                            }

                            LoginScreen(
                                navController = navController,
                                auth = auth,
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }
                                })

                        }
                        composable(Screen.RegisterScreen.route){
                            RegisterScreen(navController = navController, auth = auth)
                        }
                    }
                }
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String, auth: FirebaseAuth) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("Google Sign-In", "success")
                navigateToHome(this)
            } else {
                Log.e("Google Sign-In", task.exception.toString())
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!, auth)
            } catch (e: ApiException) {
                Log.e("Google Sign-In Error", e.toString())
            }
        }
    }
}
