package com.zeroone.wallpaperdeal.ui.screens.login

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.screens.HomeActivity
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.ButtonColorDefault

@Composable
fun LoginScreen(navController: NavController){
    val context = LocalContext.current
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
                Image(painter = painterResource(id = R.drawable.logoandtext), contentDescription = null,
                    modifier = Modifier.padding(bottom = 48.dp)
                )
                var textEmail: String by remember { mutableStateOf("") }
                TextField(
                    value = textEmail,
                    onValueChange = { textEmail = it },
                    colors = TextFieldDefaults.colors(Color.Black),
                    shape = TextFieldDefaults.shape,
                    modifier = Modifier
                        .background(color = Color.Unspecified)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        //.height(70.dp)
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
                        //.height(70.dp)
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
                val buttonColors = ButtonDefaults.buttonColors(ButtonColorDefault)
                Button(
                    onClick = { navigateToHome(context) },
                    colors = buttonColors,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = "Sign On",
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 82.dp),
                    //verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(48.dp)

                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
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
    val intent = Intent(context, HomeActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
    context.startActivity(intent)
}

