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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.ButtonLoginAndRegister
import com.zeroone.wallpaperdeal.ui.GoogleButton
import com.zeroone.wallpaperdeal.ui.MainActivity
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.screens.login.authgoogle.SignInState
import com.zeroone.wallpaperdeal.ui.theme.LoginRegisterButtonColor
import com.zeroone.wallpaperdeal.ui.theme.Purple40
import com.zeroone.wallpaperdeal.ui.theme.TextFieldBaseColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    auth: FirebaseAuth,
    viewModel: LoginViewModel = hiltViewModel(),
    state: SignInState,
    onSignInClick: () -> Unit
){
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    var forgotPass by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Color.Black,
        modifier = Modifier,
        bottomBar = {
            Column{
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Do you have a account?", color = Color.LightGray, fontSize = 14.sp)
                    TextButton(onClick = {
                        navController.navigate(Screen.RegisterScreen.route)
                    }) {
                        Text(text = "Sign In", color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(painter = painterResource(id = R.drawable.walldeallogo), contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillMaxSize(0.1f)
                )
                val textFieldColor = TextFieldDefaults.colors(
                    focusedContainerColor = TextFieldBaseColor,
                    unfocusedContainerColor = TextFieldBaseColor,
                    disabledTextColor = Color.LightGray,
                    focusedTextColor = Color.LightGray,
                    unfocusedLabelColor = Color.LightGray
                )
                var textEmail by remember { mutableStateOf("") }
                TextField(value = textEmail, colors = textFieldColor,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange ={ textEmail = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Color.LightGray ) },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                    maxLines = 1,
                    label = { Text(text = "Email", color = Color.Gray, fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.01f))
                var textPassword by remember { mutableStateOf("") }
                TextField(value = textPassword, colors = textFieldColor,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange ={ textPassword = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null, tint = Color.LightGray ) },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                    maxLines = 1,
                    label = { Text(text = "Password", color = Color.Gray, fontSize = 14.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )
                Row {
                    Spacer(modifier = Modifier.fillMaxWidth(0.45f))
                    TextButton(onClick = { forgotPass = true }) {
                        Text(text = "Forgotten password?", color = Color.LightGray)
                    }
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                ButtonLoginAndRegister(
                    modifier = Modifier.fillMaxWidth(0.33f),
                    onClicked = {
                        loading = true
                        CoroutineScope(Dispatchers.Main).launch{
                            delay(500)
                            viewModel.login(
                                textEmail = textEmail, textPassword = textPassword , context = context , loading = {loading = false}
                            )
                            delay(500)
                            loading = false
                            textPassword = ""
                        } },
                    enabled = !(textPassword.isEmpty() || textEmail.isEmpty()),
                    enabledText = "Login information cannot be empty",
                    text = "Login",
                    loadingText = "Logging in..."
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Canvas(modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .padding(top = 0.dp)) {
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
                    Spacer(modifier = Modifier.fillMaxWidth(0.12f))
                    Text(text = "OR", color = Color.LightGray, modifier = Modifier.padding(bottom = 0.dp))
                    Spacer(modifier = Modifier.fillMaxWidth(0.16f))
                    Canvas(modifier = Modifier.fillMaxWidth(1f)) {
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
                Spacer(modifier = Modifier.fillMaxHeight(0.2f))
                GoogleButton { onSignInClick() }
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            if(forgotPass){
                ForgotPasswordScreen(
                    onDismissRequest = { forgotPass = false },
                    cancelDialog = { forgotPass = false},
                    auth = auth
                )
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