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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.model.WallDeal
import com.zeroone.wallpaperdeal.model.WallpaperRequest
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.screens.share.getBitmapFromUri

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
                if (width >= 1920 && height >= 1080) {
                    buttonEnabled = true
                    Log.d("Selected photo resolution:", "${width} x ${height}")
                } else {
                    Log.e("Selected photo resolution:", "$width x $height")
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
        bottomBar = { BottomNavigationBar(selectedItem = 3, navController = navController) },
        backgroundColor = Color.Black,
    ) {
        fun uriChange(){}
        walldeal?.let { wallDealInLet ->
            val groupId = wallDealInLet.groupId
            if(!groupId.isNullOrEmpty()){
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
                    paddingValues = it,
                    selectedImageUriChange = { selectedImageUri = null }
                )
            }else{
                Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "You do not have a WallDeal", color = Color.LightGray)
                }
            }
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
    paddingValues: PaddingValues,
    selectedImageUriChange: () -> Unit
) {
    var menu by remember { mutableStateOf("request") }
    var request by remember { mutableStateOf<WallpaperRequest?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding()
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
                .background(Color.Black)
        ) {
            TextButton(
                onClick = { menu = "request" },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Requests",
                    color = if (menu == "request") Color.White else Color.Gray,
                    fontSize = 14.sp
                )
            }
            TextButton(
                onClick = {
                    if (wallDealInLet.requestId.isNullOrEmpty()) {
                        menu = "send"
                    } else {
                        Toast.makeText(
                            context, "Finalize your current wallpaper request",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Send Wallpaper Request",
                    color = if (menu == "send") Color.White else Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        when (menu) {
            "request" -> {
                if(!wallDealInLet.requestId.isNullOrEmpty()){
                    LaunchedEffect(key1 = wallDealInLet.requestId){
                        viewModel.getWallpaperRequest(requestId = wallDealInLet.requestId)
                    }
                    request = viewModel.requestState.value
                    request?.let {
                        Column(modifier = Modifier
                            .fillMaxHeight(1f)
                            .fillMaxWidth()){
                            WallDealRequestOfWallpaper(
                                request = it,
                                viewModel = viewModel,
                                userId = userId
                            )
                        }
                    }
                }
            }
            "send" -> {
                Column(modifier = Modifier.fillMaxHeight(1f).fillMaxWidth()){
                    SendWallpaperRequestContent(
                        singlePhotoPickerLauncher = singlePhotoPickerLauncher,
                        selectedImageUri = selectedImageUri,
                        buttonEnabled = buttonEnabled,
                        storage = storage,
                        userId = userId,
                        viewModel = viewModel,
                        wallDealInLet = wallDealInLet,
                        context = context,
                        selectedImageUriChange = { selectedImageUriChange() },
                        menuChange = { menu = "request" }
                    )
                }
            }
            else -> {}
        }
    }
}