package com.zeroone.wallpaperdeal.ui.screens.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.model.User
import com.zeroone.wallpaperdeal.model.UserDetail
import com.zeroone.wallpaperdeal.ui.MainActivity
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.LoginRegisterButtonColor
import com.zeroone.wallpaperdeal.ui.theme.Purple40
import com.zeroone.wallpaperdeal.ui.theme.TextFieldBaseColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController,viewModel: RegisterViewModel = hiltViewModel(), auth: FirebaseAuth){
    val context = LocalContext.current
    var registerButtonEnabled by remember { mutableStateOf<Boolean>(false)}
    var loading by remember { mutableStateOf<Boolean>(false) }
    Scaffold(
        containerColor = Color.Black
    ) {
        Column(
            modifier = Modifier.padding(it).fillMaxSize(),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.walldeallogo),
                contentDescription = null, modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(0.1f)
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
            var textUsername by remember { mutableStateOf("") }
            TextField(value = textUsername, colors = textFieldColor,
                shape = RoundedCornerShape(20.dp),
                onValueChange ={ textUsername = it },
                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.LightGray ) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                maxLines = 1,
                label = { Text(text = "Username", color = Color.Gray, fontSize = 14.sp) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
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
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            var textPasswordRepeat by remember { mutableStateOf("") }
            TextField(value = textPasswordRepeat, colors = textFieldColor,
                shape = RoundedCornerShape(20.dp),
                onValueChange ={ textPasswordRepeat = it },
                leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null, tint = Color.LightGray ) },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                maxLines = 1,
                label = { Text(text = "Re-Password", color = Color.Gray, fontSize = 14.sp) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.01f))
            registerButtonEnabled =
                !(textPassword != textPasswordRepeat || textPassword.isEmpty() || textPasswordRepeat.isEmpty()
                        || textUsername.isEmpty() || textEmail.isEmpty())
            Button(enabled = registerButtonEnabled,
                onClick = {
                    loading = true
                    if(textUsername.length <= 3){
                        Toast.makeText(context,"Username must be at least 4 characters",Toast.LENGTH_SHORT).show()
                        loading = false
                    }
                    else if(textPassword.length <= 7){
                        Toast.makeText(context,"Password must be at least 8 characters",Toast.LENGTH_SHORT).show()
                        loading = false
                    }
                    else if(!(textEmail.contains("@"))){
                        Toast.makeText(context,"Please enter a valid email address",Toast.LENGTH_SHORT).show()
                        loading = false
                    }
                    else{
                        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener {
                            if(it.isSuccessful){
                                auth.currentUser?.let {
                                    val userDetail = UserDetail(null, emptyList(), emptyList(), emptyList())
                                    val user = User(
                                        userId = it.uid, textEmail,
                                        username = textUsername,
                                        wallDealId = "",
                                        userDetail = userDetail,
                                        fcmToken = ""
                                    )
                                    CoroutineScope(Dispatchers.Main).launch{
                                        viewModel.saveUserToDb(user)
                                    }
                                    sendVerificationEmail(user = it)
                                    //navigateHomeActivity(context)
                                    navController.navigate(Screen.LoginScreen.route)
                                }
                                loading = false
                                Log.d("UserAuth:", "succes")

                            }else{
                                loading = false
                                Toast.makeText(context,it.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                                Log.d("UserAuth:", "failure: ", it.exception)
                            }
                        }
                    }
                },
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(LoginRegisterButtonColor)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(30.dp)
                    )
                }else{
                    Text(
                        text = "Register",
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.White
                    )
                }
            }

        }
    }
}
fun navigateHomeActivity(context: Context){
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
    context.startActivity(intent)
    (context as Activity).finish()
}

fun sendVerificationEmail(user: FirebaseUser){
    user.sendEmailVerification().addOnCompleteListener {
        Log.d("VerifyEmail", "sent")
    }.addOnFailureListener {
        Log.e("VerifyEmail", "did not send")
    }
}