package com.zeroone.wallpaperdeal.ui.screens.couple

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.data.model.UserDTO
import com.zeroone.wallpaperdeal.data.model.WallDeal
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest
import com.zeroone.wallpaperdeal.ui.theme.BlueTwitter
import com.zeroone.wallpaperdeal.ui.theme.TextFieldBaseColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun SendWallpaperRequestContent(
    singlePhotoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    selectedImageUri: Uri?,
    buttonEnabled: Boolean,
    storage: FirebaseStorage,
    userId: String,
    viewModel: CoupleOprtViewModel,
    wallDealInLet: WallDeal,
    menuChange: () -> Unit,
    selectedImageUriChange: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }
    LaunchedEffect(key1 = true){
        singlePhotoPickerLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
        // adjust the position of the elements
    ) {
        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(0.85f)
                    .padding(top = 0.dp, bottom = 64.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth()
                    .padding(top = 0.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Photo",
                    color = Color.LightGray,
                    fontSize = 18.sp,
                    modifier = Modifier
                )

                IconButton(
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .size(60.dp)
                        .padding(top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_image),
                        contentDescription = null,
                        tint = Color.LightGray, modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.25f))
        var text by remember { mutableStateOf("") }
        Row {
            val maxLength = 120
            TextField(
                value = text,
                onValueChange = { if(text.length <= 120){
                                    text = it
                                }},
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = TextFieldBaseColor,
                    textColor = Color.LightGray,
                    disabledTextColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.LightGray,
                    focusedIndicatorColor = Color.LightGray
                ),
                maxLines = 2,
                label = { Text(text = "Message", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = CircleShape

            )

            IconButton(
                onClick = {
                    isLoading = true
                    val storageRef = storage.reference.child("wallpaper_request${wallDealInLet.groupId}/${UUID.randomUUID()}")
                    val uri = selectedImageUri!!
                    storageRef.putFile(uri)
                        .addOnSuccessListener {
                            Log.e("Request image upload", "success")
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                imageUrl =uri.toString()
                                var senderUser: UserDTO? = null
                                var receiverUser: UserDTO? = null
                                if(wallDealInLet.user1.userId.equals(userId)){
                                    senderUser = wallDealInLet.user1
                                    receiverUser = wallDealInLet.user2
                                }else{
                                    senderUser = wallDealInLet.user2
                                    receiverUser = wallDealInLet.user1
                                }
                                senderUser.let {
                                    val request = WallpaperRequest(
                                        message = text,
                                        senderUser = it,
                                        receiverUser = receiverUser,
                                        imageUrl = imageUrl,
                                        wallpaperRequestId = it.userId + receiverUser.userId
                                    )
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            viewModel.sendWallpaperRequest(userId = userId, request = request)
                                        }finally {
                                            isLoading = false
                                            menuChange()
                                            selectedImageUriChange()
                                        }
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("BlurImage", e.message.toString())
                        }
                },
                enabled = buttonEnabled && !isLoading,
            ) {
                when (isLoading) {
                    true -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    false -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_send),
                            contentDescription = null,
                            tint = BlueTwitter
                        )
                    }
                }
            }
        }
    }
}