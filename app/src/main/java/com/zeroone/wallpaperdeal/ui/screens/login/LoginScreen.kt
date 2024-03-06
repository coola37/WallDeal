package com.zeroone.wallpaperdeal.ui.screens.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.MainActivity
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.Purple40
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController,auth: FirebaseAuth){
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    Box (modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)){
        Box(modifier = Modifier
            .fillMaxSize()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.walldeallogo), contentDescription = null,
                    modifier = Modifier.padding(bottom = 48.dp)
                )
                var textEmail: String by remember { mutableStateOf("") }
                TextField(
                    value = textEmail,
                    onValueChange = { textEmail = it },
                    colors = TextFieldDefaults.colors(Color.LightGray),
                    shape = TextFieldDefaults.shape,
                    modifier = Modifier
                        .background(color = Color.Unspecified)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .focusRequester(FocusRequester())
                        .padding(horizontal = 16.dp),  // Adjust padding as needed
                    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
                    label = { Text(text = "E-Mail") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true
                )

                var textPassword: String by remember { mutableStateOf("") }
                TextField(
                    value = textPassword,
                    onValueChange = { textPassword = it },
                    colors = TextFieldDefaults.colors(Color.LightGray),
                    shape = TextFieldDefaults.shape,
                    modifier = Modifier
                        .background(color = Color.Unspecified)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .focusRequester(FocusRequester())
                        .padding(horizontal = 16.dp),  // Adjust padding as needed
                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                    label = { Text(text = "Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )
                val buttonColors = ButtonDefaults.buttonColors(Purple40)
                Button(
                    onClick = {
                        loading = true
                        CoroutineScope(Dispatchers.Main).launch{
                            delay(1000)
                            auth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener {
                                if(it.isSuccessful){
                                    loading = false
                                    Log.d("signInWithEmail:", "success")
                                    navigateToHome(context)
                                }else{
                                    loading = false
                                    Log.e("signInWithEmail:", it.exception.toString())
                                    Toast.makeText(context, "Check your login information", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        },
                    enabled = !(textPassword.isEmpty() || textEmail.isEmpty()) ,
                    colors = buttonColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
                ) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }else{
                        Text(
                            text = "Login",
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = Color.White
                        )
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 82.dp),
                    horizontalArrangement = Arrangement.spacedBy(48.dp)

                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                            .size(70.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                painter = painterResource(id = R.drawable.google),
                                contentDescription = "Icon",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(500.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(70.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Icon(
                                painter = painterResource(id = R.drawable.facebook),
                                contentDescription = "Icon",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(500.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(150.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    Canvas(modifier = Modifier.fillMaxWidth()) {
                        val start = Offset(0f, size.height / 2)
                        val end = Offset(size.width, size.height / 2)
                        drawLine(
                            start = start,
                            end = end,
                            color = Color.Gray,
                            strokeWidth = 5f,
                            cap = StrokeCap.Round
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 64.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Do you have a account?", color = Color.LightGray, fontSize = 16.sp)
                    TextButton(onClick = {
                        navController.navigate(Screen.RegisterScreen.route)
                    }) {
                        Text(text = "Sign In", color = Color.White, fontSize = 18.sp)
                    }
                }

            }
        }
    }
}
fun navigateToHome(context: Context){
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
    context.startActivity(intent)
    (context as Activity).finish()
}