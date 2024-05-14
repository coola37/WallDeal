package com.zeroone.wallpaperdeal.ui.screens.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.ui.LoginActivity
import com.zeroone.wallpaperdeal.ui.theme.PurpleGrey80
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DeleteAccountDialogScreen(
    auth: FirebaseAuth,
    cancelDialog: () -> Unit,
    onDismissRequest: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = { onDismissRequest() }) {
        val cardColors = CardColors(
            containerColor = PurpleGrey80,
            contentColor = PurpleGrey80,
            disabledContainerColor = PurpleGrey80,
            disabledContentColor = PurpleGrey80
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = cardColors
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = "Are you sure you want to delete your account?", modifier = Modifier.padding(16.dp),
                    color = Color.DarkGray, fontSize = 12.sp, textAlign = TextAlign.Center
                )
                val textFieldColor = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledTextColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedLabelColor = Color.LightGray
                )
                var textPassword by remember{ mutableStateOf("") }
                TextField(value = textPassword, colors = textFieldColor,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange ={ textPassword = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null, tint = Color.DarkGray ) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                    maxLines = 3,
                    label = { Text(text = "Desc.", color = Color.Gray, fontSize = 12.sp) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss", color = Color.DarkGray)
                    }
                    TextButton(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                auth.currentUser?.let {
                                    val credential = auth.signInWithEmailAndPassword(it.email!!, textPassword)
                                    credential?.addOnCompleteListener { task ->

                                        if(task.isSuccessful){
                                            it.delete().addOnCompleteListener { taskVoid ->
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    profileViewModel.deleteAccount(userId = it.uid)
                                                    delay(3000) }
                                                navigateToLogin(context)
                                            }
                                        }
                                    }
                                }
                            cancelDialog()
                        }},
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Delete", color = Color.Red)
                    }
                }
            }
        }
    }
}

fun navigateToLogin(context: Context){
    val intent = Intent(context, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
    context.startActivity(intent)
    (context as Activity).finish()
}