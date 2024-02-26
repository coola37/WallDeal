package com.zeroone.wallpaperdeal.ui.screens.register

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
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.ButtonColorDefault
import com.zeroone.wallpaperdeal.ui.theme.Purple40

@Composable
public fun RegisterScreen(navController: NavController){
    Box(modifier = Modifier .fillMaxSize().background(Color.Black))
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
                    //.height(70.dp)
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
                    //.height(70.dp)
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
            Button(
                onClick = { navController.navigate(Screen.RegisterScreen.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 32.dp, end = 32.dp),
                colors = buttonColors
            ) {
                Text(
                    text = "Sign In",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(150.dp))
        }
    }

}
