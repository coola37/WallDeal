package com.zeroone.wallpaperdeal.ui.screens.login

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.ui.theme.PurpleGrey80

@Composable
fun ForgotPasswordScreen(
    onDismissRequest: () -> Unit,
    cancelDialog: () -> Unit,
    auth: FirebaseAuth
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = {/* onDismissRequest()*/ }) {
        val cardColors = CardColors(
            containerColor = PurpleGrey80,
            contentColor = PurpleGrey80,
            disabledContainerColor = PurpleGrey80,
            disabledContentColor = PurpleGrey80
        )
        var sent by remember { mutableStateOf(true) }
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
                    text = "Enter your e-mail address",
                    modifier = Modifier.padding(16.dp), color = Color.DarkGray
                )
                val textFieldColor = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledTextColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedLabelColor = Color.LightGray
                )
                var email by remember{ mutableStateOf("") }
                TextField(value = email, colors = textFieldColor,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange ={ email = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = Color.DarkGray ) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                    maxLines = 3,
                    label = { Text(text = "Email", color = Color.Gray, fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
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
                        Text("Dismiss", color = Color.Gray)
                    }
                    TextButton(
                        onClick = {
                          auth.sendPasswordResetEmail(email).addOnCompleteListener {
                              Toast.makeText(context, "Password reset link has been sent, please check your email.",
                                  Toast.LENGTH_LONG).show()
                              cancelDialog()
                          }.addOnFailureListener {
                              Toast.makeText(context, "Please check your email address",
                                  Toast.LENGTH_LONG).show()
                          }
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Send", color = Color.Black)
                    }
                }
            }
        }
    }
}