package com.zeroone.wallpaperdeal.ui.screens.walldeal

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.model.WallDeal
import com.zeroone.wallpaperdeal.model.WallpaperRequest
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.screens.share.bitmapToByteArray
import com.zeroone.wallpaperdeal.ui.screens.share.createGradiant
import com.zeroone.wallpaperdeal.ui.screens.share.getBitmapFromUri
import com.zeroone.wallpaperdeal.ui.theme.BlueTwitter
import com.zeroone.wallpaperdeal.ui.theme.TextFieldBaseColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun WallDealScreen(
    auth: FirebaseAuth,
    navController: NavController,
    storage: FirebaseStorage,
    viewModel: WallDealViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val userId = auth.uid!!
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var buttonEnabled by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            selectedImageUri?.let { uri ->
                val bitmap = getBitmapFromUri(context, uri)
                val width = bitmap?.width ?: 0
                val height = bitmap?.height ?: 0
                if (width >= 1000 && height >= 1800) {
                    buttonEnabled = true
                    Log.d("Selected photo resolution:", "${width.toString()} x ${height.toString()}")
                } else {
                    Log.e("Selected photo resolution:", "${width.toString()} x ${height.toString()}")
                    buttonEnabled = false
                    Toast.makeText(
                        context,
                        "The selected image does not meet the minimum resolution requirement of 1080x1920 pixels.",
                        Toast.LENGTH_SHORT
                    ).show()
                    selectedImageUri = null
                }
            } ?: run {
                buttonEnabled = false
            }
        }
    )

    LaunchedEffect(key1 = userId) {
        viewModel.getWallDeal(userId = userId)
    }
    val wallDealState = viewModel.wallDealState
    val walldeal = wallDealState.value

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedItem = 3, navController = null) },
        backgroundColor = Color.Black,
    ) {
        walldeal?.let { wallDealInLet ->
            WallDealContent(
                context = context,
                wallDealInLet = wallDealInLet,
                singlePhotoPickerLauncher = singlePhotoPickerLauncher,
                selectedImageUri = selectedImageUri,
                buttonEnabled = buttonEnabled,
                isLoading = isLoading,
                storage = storage,
                userId = userId,
                imageUrl = imageUrl,
                viewModel = viewModel,
                paddingValues = it
            )
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun WallDealContent(
    context: Context,
    wallDealInLet: WallDeal,
    singlePhotoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    selectedImageUri: Uri?,
    buttonEnabled: Boolean,
    isLoading: Boolean,
    storage: FirebaseStorage,
    userId: String,
    imageUrl: String,
    viewModel: WallDealViewModel,
    paddingValues: PaddingValues
) {
    var menu by remember { mutableStateOf("request") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            TextButton(
                onClick = { menu = "request" },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Requests",
                    color = if (menu == "request") Color.White else Color.Gray,
                    fontSize = 16.sp
                )
            }
            TextButton(
                onClick = {
                    if (wallDealInLet.request == null) {
                        menu = "send"
                    } else {
                        Toast.makeText(
                            context, "Before you can send a wallpaper request, you must end the current request.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Send Wallpaper Request",
                    color = if (menu == "send") Color.White else Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        when (menu) {
            "request" -> {
                wallDealInLet.request?.let {
                    WallDealRequestOfWallpaper(request = wallDealInLet.request, viewModel = viewModel, userId = userId)
                }
            }
            "send" -> {
                SendWallpaperRequestContent(
                    singlePhotoPickerLauncher = singlePhotoPickerLauncher,
                    selectedImageUri = selectedImageUri,
                    buttonEnabled = buttonEnabled,
                    storage = storage,
                    userId = userId,
                    viewModel = viewModel,
                    wallDealInLet = wallDealInLet,
                    context = context
                ){ menu = "request"}
            }
            else -> {}
        }
    }
}

@Composable
fun SendWallpaperRequestContent(
    singlePhotoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    selectedImageUri: Uri?,
    buttonEnabled: Boolean,
    storage: FirebaseStorage,
    userId: String,
    viewModel: WallDealViewModel,
    wallDealInLet: WallDeal,
    context: Context,
    menuChange: () -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        val textFieldColor = TextFieldDefaults.textFieldColors(
            backgroundColor = TextFieldBaseColor,
            textColor = Color.LightGray,
            disabledTextColor = Color.White,
            unfocusedLabelColor = Color.Gray,
            focusedLabelColor = Color.LightGray,
            focusedIndicatorColor = Color.LightGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight(0.90f)
                    .fillMaxWidth(0.66f)
            )
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Text(
                text = "Select Wallpaper",
                color = Color.LightGray,
                fontSize = 20.sp
            )
            Row {
                IconButton(
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_image),
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(100.dp)
                    )
                }
            }
        }

        var text by remember { mutableStateOf("") }
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                colors = textFieldColor,
                label = { Text(text = "Message", fontSize = 18.sp) },
                modifier = Modifier.weight(1f),
                maxLines = 5
            )

            IconButton(
                onClick = {
                    isLoading = true
                    val storageRef = storage.reference.child("walldeal_request${wallDealInLet.groupId}/${UUID.randomUUID()}")
                    val uri = selectedImageUri!!
                    storageRef.putFile(uri)
                        .addOnSuccessListener {
                            Log.e("Request image upload", "success")
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                imageUrl = uri.toString()
                                val senderUser = wallDealInLet.groupMembers.find { it.userId == userId }
                                val listReceiver = wallDealInLet.groupMembers.filterNot { it == senderUser }
                                senderUser?.let {
                                    val request = WallpaperRequest(
                                        message = text,
                                        senderUser = it,
                                        receiverUsers = listReceiver,
                                        imageUrl = imageUrl
                                    )
                                    CoroutineScope(Dispatchers.IO).launch {
                                        viewModel.sendWallpaperRequest(userId = userId, request = request)
                                        delay(2000)
                                        isLoading = false
                                        menuChange()
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("BlurImage", e.message.toString())
                        }
                },
                enabled = buttonEnabled
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
        Spacer(modifier = Modifier.height(32.dp))
    }
}
@Composable
fun WallDealRequestOfWallpaper(request: WallpaperRequest, viewModel: WallDealViewModel, userId: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            AsyncImage(
                model = request.senderUser.userDetail?.profilePhoto,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
            )
            Text(
                text = request.senderUser.username,
                color = Color.LightGray,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 16.dp, start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.height(600.dp)
        ) {
            AsyncImage(
                model = request.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.66f)
                    .fillMaxSize(0.75f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            request.message?.let {
                Text(
                    text = it,
                    fontSize = 20.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Row {
                if (userId != request.senderUser.userId) {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.walldeal_ok),
                            contentDescription = null,
                            tint = Color.Unspecified
                        )
                    }
                    Spacer(modifier = Modifier.width(64.dp))
                }
                IconButton(
                    onClick = {
                        viewModel.cancelPost(userId = userId, request = request)
                        Log.e("click", "true")
                    },
                    modifier = Modifier
                        .size(50.dp)
                        .padding(top = 8.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cancel),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}

