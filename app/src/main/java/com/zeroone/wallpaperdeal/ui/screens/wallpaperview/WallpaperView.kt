package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.data.model.LikeRequest
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.screens.ScreenCallback
import com.zeroone.wallpaperdeal.utils.downloadWallpaper
import com.zeroone.wallpaperdeal.utils.setWallpaper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WallpaperViewScreen(
    navController: NavController,
    viewModel: WallpaperViewViewModel = hiltViewModel(),
    callback: ScreenCallback,
    auth: FirebaseAuth
) {
    val context = LocalContext.current
    val wallpaperId = navController.currentBackStackEntry?.arguments?.getString("wallpaperId")
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    wallpaperId?.let {
        var wallpaper by remember { mutableStateOf<Wallpaper?>(null) }
        var checkLike by remember { mutableStateOf<Boolean>(false) }
        var userId by remember { mutableStateOf<String>("") }
        LaunchedEffect(key1 = wallpaperId){
            viewModel.fetchWallpaper(wallpaperId = wallpaperId)
            auth.uid?.let{
                userId = it
                viewModel.checkLike(wallpaperId = wallpaperId, userId = it)
            }
        }
        var expanded by remember { mutableStateOf<Boolean>(false) }

        checkLike = viewModel.checkLikeState.value
        wallpaper = viewModel.wallpaperState.value

        wallpaper?.let {wallpaper->

            LaunchedEffect(key1 = wallpaper.imageUrl){
                val request = ImageRequest.Builder(context)
                    .data(wallpaper.imageUrl)
                    .build()

                val result = context.imageLoader.execute(request)
                imageBitmap = if (result.drawable is BitmapDrawable) {
                    (result.drawable as BitmapDrawable).bitmap
                } else {
                    null
                }
            }

            val keyboardController = LocalSoftwareKeyboardController.current
            val coroutineScope = rememberCoroutineScope()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, bottom = 120.dp, start = 16.dp, end = 16.dp)
            ) {
                AsyncImage(
                    model = wallpaper.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {

                    wallpaper.description?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 20.sp,
                        )
                    }

                    Text(
                        text = "${wallpaper.likeCount} users liked it",
                        color = Color.LightGray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )
                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .padding(bottom = 8.dp)
                            .width(60.dp)
                            .height(50.dp),
                        onClick = { expanded = true }
                    ) {
                       if(expanded){
                           Icon(
                               tint = Color.Unspecified,
                               painter = painterResource(id = R.drawable.ic_cancel),
                               modifier = Modifier
                                   .clip(CircleShape)
                                   .fillMaxSize(),
                               contentDescription = null
                           )

                       }else{
                           Icon(
                               tint = Color.Unspecified,
                               painter = painterResource(id = R.drawable.options_icon),
                               modifier = Modifier
                                   .clip(CircleShape)
                                   .fillMaxSize(),
                               contentDescription = null
                           )
                       }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            CoroutineScope(Dispatchers.Main).launch {
                                val likeRequest = LikeRequest(userId)
                                viewModel.likeOrDislike(wallpaperId = wallpaperId, likeRequest = likeRequest)
                                //viewModel.checkLike(wallpaperId = wallpaperId, userId = userId )
                            }
                        }) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Log.e("checkLike Control", checkLike.toString())
                                if(!checkLike){
                                    Text("Like", fontSize = 18.sp)
                                }else{
                                    Text("Dislike", color = Color.Red, fontSize = 20.sp)
                                }
                            }
                        }
                        DropdownMenuItem(onClick = {
                            expanded = false
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.SET_WALLPAPER) != PackageManager.PERMISSION_GRANTED
                            ) {
                                callback.onSetWallpaperClick(imageBitmap!!)
                            } else {
                                // Permission is already granted, set the wallpaper here
                                setWallpaper(context = context, bitmap = imageBitmap!!)
                            }
                        }) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Set as wallpaper", fontSize = 18.sp)
                            }
                        }
                        DropdownMenuItem(onClick = {
                            expanded = false
                            Log.e("ImageUrl", wallpaper.imageUrl)
                            downloadWallpaper(context, wallpaper.imageUrl)

                        }) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Download wallpaper", fontSize = 18.sp)
                            }
                        }
                    }
                }
            }
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    expanded = false
                    keyboardController?.hide()
                }
            }
            BackHandler(onBack = {
                if (expanded) {
                    expanded = false
                } else {
                    navController.popBackStack()
                }
            })
        }
    }
}
