package com.zeroone.wallpaperdeal.ui.screens.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.ui.theme.ActiveButton
import com.zeroone.wallpaperdeal.ui.theme.PurpleGrey80
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(
    onDismissRequest: () -> Unit,
    cancelDialog: () -> Unit,
    auth: FirebaseAuth,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        val context = LocalContext.current
        val cardColors = CardColors(
            containerColor = PurpleGrey80,
            contentColor = PurpleGrey80,
            disabledContainerColor = PurpleGrey80,
            disabledContentColor = PurpleGrey80
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
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
                Spacer(modifier = Modifier.height(0.dp))
                Text(text = "Change password", color = Color.DarkGray)
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { auth.sendPasswordResetEmail(auth.currentUser?.email.toString()).addOnCompleteListener {
                    Toast.makeText(context, "The password reset link has been sent to your email address", Toast.LENGTH_LONG).show()
                    onDismissRequest()
                }.addOnFailureListener {
                    Toast.makeText(context, "The password reset link could not be sent", Toast.LENGTH_LONG).show()
                }
                }) {
                    Text(
                        text = "Forgot password",
                        modifier = Modifier.padding(16.dp), color = Color.Blue
                    )
                }
                val textFieldColor = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledTextColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedLabelColor = Color.LightGray
                )
                var textCurrentPassword by remember{ mutableStateOf("") }
                TextField(value = textCurrentPassword, colors = textFieldColor,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange ={ textCurrentPassword = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null, tint = Color.Gray ) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                    maxLines = 3,
                    label = { Text(text = "Current Password", color = Color.Gray, fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )
                var textNewPassword by remember{ mutableStateOf("") }
                TextField(value = textNewPassword, colors = textFieldColor,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange ={ textNewPassword = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null, tint = Color.Gray ) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                    maxLines = 3,
                    label = { Text(text = "New Password", color = Color.Gray, fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )
                var textNewPassword2 by remember{ mutableStateOf("") }
                TextField(value = textNewPassword2, colors = textFieldColor,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange ={ textNewPassword2 = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Password, contentDescription = null, tint = Color.Gray ) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                    maxLines = 3,
                    label = { Text(text = "Repeat New Password", color = Color.Gray, fontSize = 14.sp) },
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
                            CoroutineScope(Dispatchers.Main).launch {
                                if(textNewPassword.length > 8){
                                    val currentUser = auth.currentUser
                                    currentUser?.let {
                                        val credential = EmailAuthProvider.getCredential(it.email!!,textCurrentPassword)
                                        it.reauthenticate(credential).addOnCompleteListener { task ->
                                            if(task.isSuccessful){
                                                currentUser.updatePassword(textNewPassword)
                                                    .addOnCompleteListener { task ->
                                                        if (task.isSuccessful) {
                                                            Toast.makeText(context,"Password change successful.",Toast.LENGTH_SHORT).show()
                                                            cancelDialog()
                                                        } else {
                                                            Toast.makeText(context,"Password change failed!",Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                            }else{
                                                Log.e("CurrentUserVerification", task.exception.toString())
                                                Toast.makeText(context,"Password change failed. Check your current password",Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                }else{
                                    Toast.makeText(context,"Password must be at least 8 characters",Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier.padding(8.dp),
                        enabled = textNewPassword.equals(textNewPassword2)
                    ) {
                        Text("Change", color = ActiveButton)
                    }
                }
            }
        }
    }
}

/*/@Preview
@Composable
fun PreviewChangePassword(){
    //ChangePasswordScreen()
}*/
