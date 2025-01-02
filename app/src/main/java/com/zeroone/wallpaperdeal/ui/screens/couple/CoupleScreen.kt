package com.zeroone.wallpaperdeal.ui.screens.couple

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.zeroone.wallpaperdeal.data.model.Couple
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.screens.share.getBitmapFromUri
import com.zeroone.wallpaperdeal.utils.Constant.HEIGHT_PX
import com.zeroone.wallpaperdeal.utils.Constant.RESOLUTION_ERROR
import com.zeroone.wallpaperdeal.utils.Constant.WEIGHT_PX

@Composable
fun WallDealScreen(
    auth: FirebaseAuth,
    navController: NavController,
    storage: FirebaseStorage,
    viewModel: CoupleOprtViewModel = hiltViewModel()
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
                if (width >= WEIGHT_PX && height >= HEIGHT_PX) {
                    buttonEnabled = true
                    Log.d("Selected photo resolution:", "${width} x ${height}")
                } else {
                    Log.e("Selected photo resolution:", "$width x $height")
                    buttonEnabled = false
                    Toast.makeText(context, RESOLUTION_ERROR, Toast.LENGTH_SHORT).show()
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
    val wallDealState = viewModel.coupleState
    val wallDeal = wallDealState.value
    Log.e("Couple Control", wallDeal.toString())

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedItem = 3, navController = navController) },
        backgroundColor = Color.Black,
    ) {
        fun uriChange(){}
        wallDeal?.let { wallDealInLet ->
            val groupId = wallDealInLet.groupId
            if(!groupId.isNullOrEmpty()){
                WallDealContent(
                    context = context,
                    coupleInLet = wallDealInLet,
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
                    Text(text = "You do not have a couple", color = Color.LightGray)
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
    coupleInLet: Couple,
    singlePhotoPickerLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    selectedImageUri: Uri?,
    buttonEnabled: Boolean,
    isLoading: Boolean,
    storage: FirebaseStorage,
    userId: String,
    imageUrl: String,
    viewModel: CoupleOprtViewModel,
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
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text(
                    text = "Request",
                    color = if (menu == "request") Color.White else Color.Gray,
                    fontSize = 14.sp
                )
            }
            TextButton(
                onClick = {
                    if (coupleInLet.requestId.isNullOrEmpty()) {
                        menu = "send"
                    } else {
                        Toast.makeText(context, "Finalize your current wallpaper request",
                            Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Send Request",
                    color = if (menu == "send") Color.White else Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        when (menu) {
            "request" -> {
                if(!coupleInLet.requestId.isNullOrEmpty()){
                    LaunchedEffect(key1 = coupleInLet.requestId){
                        viewModel.getWallpaperRequest(requestId = coupleInLet.requestId)
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
                        coupleInLet = coupleInLet,
                        selectedImageUriChange = { selectedImageUriChange() },
                        menuChange = { menu = "request" }
                    )
                }
            }
            else -> {}
        }
    }
}