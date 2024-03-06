package com.zeroone.wallpaperdeal.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.screens.login.LoginScreen
import com.zeroone.wallpaperdeal.ui.screens.login.navigateToHome
import com.zeroone.wallpaperdeal.ui.screens.register.RegisterScreen
import com.zeroone.wallpaperdeal.ui.theme.WallpaperDealTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
private const val RC_SIGN_IN = 123

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    @Inject
    lateinit var auth: FirebaseAuth

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
                            LoginScreen(navController = navController, auth = auth)
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
