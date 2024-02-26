package com.zeroone.wallpaperdeal.ui.screens.share

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.Purple40

@Composable
fun ShareScreen(navController: NavController){
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var buttonEnabled by remember {
        mutableStateOf<Boolean>(false)
    }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri
            if(selectedImageUri != null){
                buttonEnabled = true
                Log.e("Enabled", buttonEnabled.toString() + selectedImageUri.toString())
            }else{
                buttonEnabled = false
            }
        }

    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(Modifier.weight(1f)) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = {navController.navigate(Screen.HomeScreen.route)}) {
                Text(text = "Back", color = Color.White, fontSize = 20.sp)
            }
            TextButton(onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text(text = "Pick Photo", color = Purple40, fontSize = 20.sp)
            }

            TextButton(onClick = {Log.e("buttonrnabled", "click")}, enabled = buttonEnabled) {
                if(!buttonEnabled){
                    Text(text = "Next", color = Color.Gray, fontSize = 20.sp)
                }else{
                    Text(text = "Next", color = Color.White, fontSize = 20.sp)
                }
            }
        }
    }
}
