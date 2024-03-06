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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.ui.MainActivity
import com.zeroone.wallpaperdeal.ui.theme.Purple40
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController,viewModel: RegisterViewModel = hiltViewModel(), auth: FirebaseAuth){
    val context = LocalContext.current
    var registerButtonEnabled by remember { mutableStateOf<Boolean>(false)}
    var loading by remember { mutableStateOf<Boolean>(false) }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black))
    Box(modifier = Modifier.fillMaxSize()){
        Column( modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Bottom)){

            Image(
                painter = painterResource(id = R.drawable.walldeallogo),
                contentDescription = null,
            )

            var textUsername: String by remember { mutableStateOf("") }
            TextField(
                value = textUsername,
                onValueChange = { textUsername = it },
                colors = TextFieldDefaults.colors(Color.LightGray),
                shape = TextFieldDefaults.shape,
                modifier = Modifier
                    .background(color = Color.Unspecified)
                    .fillMaxWidth()
                    .focusRequester(FocusRequester())
                    .padding(horizontal = 16.dp),  // Adjust padding as needed
                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                label = { Text(text = "Username") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
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
                    .focusRequester(FocusRequester())
                    .padding(horizontal = 16.dp),
                leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                label = { Text(text = "Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            var textPasswordRepeat: String by remember { mutableStateOf("") }
            TextField(
                value = textPasswordRepeat,
                onValueChange = { textPasswordRepeat = it },
                colors = TextFieldDefaults.colors(Color.LightGray),
                shape = TextFieldDefaults.shape,
                modifier = Modifier
                    .background(color = Color.Unspecified)
                    .fillMaxWidth()
                    .focusRequester(FocusRequester())
                    .padding(horizontal = 16.dp),  // Adjust padding as needed
                leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                label = { Text(text = "Confirm-Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            val buttonColors = ButtonDefaults.buttonColors(Purple40)
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
                    else if(!(textEmail.contains(".com"))){
                        Toast.makeText(context,"Please enter a valid email address",Toast.LENGTH_SHORT).show()
                        loading = false
                    }
                    else{
                        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener {
                            if(it.isSuccessful){
                                loading = false
                                Log.d("UserAuth:", "succes")
                                auth.currentUser.let{
                                    var user = User(auth.currentUser?.uid.toString(), textEmail, textUsername, null)
                                    CoroutineScope(Dispatchers.IO).launch{
                                        viewModel.saveUserToDb(user)
                                    }
                                    navigateHomeActivity(context)
                                }
                            }else{
                                loading = false
                                Toast.makeText(context,it.exception.toString(),Toast.LENGTH_SHORT).show()
                                Log.d("UserAuth:", "failure: ", it.exception)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp),
                colors = buttonColors
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

            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}
fun navigateHomeActivity(context: Context){
    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
    context.startActivity(intent)
    (context as Activity).finish()
}