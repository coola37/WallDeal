package com.zeroone.wallpaperdeal.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.theme.Purple40
import com.zeroone.wallpaperdeal.ui.theme.Purple80

@Composable
fun SettingsScreen(
    auth: FirebaseAuth,
    navController: NavController
){
    var dialogPassword by remember { mutableStateOf(false) }
    var dialogDeleteAccount by remember { mutableStateOf(false) }
    var dialogSignOut by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navController.navigateUp() },
                    modifier = Modifier.fillMaxWidth(0.1f)
                    ) {
                    Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                        tint = Color.LightGray)
                }
                Spacer(modifier = Modifier.fillMaxWidth(0.33f))
                Text(text = "Settings", color = Purple80, fontSize = 24.sp,
                    modifier = Modifier.padding(top = 12.dp))
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(it)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            TextButton(onClick = { dialogPassword = true }, modifier = Modifier) {
                Text(text = "Change Password", color = Color.LightGray, fontSize = 16.sp,
                    modifier = Modifier.padding(start = 12.dp))
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.02f))
            TextButton(onClick = {
                                 dialogDeleteAccount = true

            }, modifier = Modifier) {
                Text(text = "Delete Account", color = Color.Red, fontSize = 16.sp,
                    modifier = Modifier.padding(start = 12.dp))
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.05f))
            TextButton(onClick = { auth.signOut() }, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Sign Out", color = Purple40, fontSize = 16.sp,
                    modifier = Modifier.padding(start = 12.dp))
            }
            if(dialogPassword){
                ChangePasswordScreen(
                    onDismissRequest = { dialogPassword = false },
                    cancelDialog = { dialogPassword = false },
                    auth = auth
                )
            }
            if(dialogDeleteAccount){
                DeleteAccountDialogScreen(
                    onDismissRequest = { dialogDeleteAccount = false },
                    cancelDialog = { dialogDeleteAccount = false },
                    auth = auth
                )
            }
        }
    }
}