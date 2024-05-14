package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.data.model.LikeRequest
import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.screens.ScreenCallback
import com.zeroone.wallpaperdeal.ui.theme.ActiveButton
import com.zeroone.wallpaperdeal.ui.theme.LikeBlue
import com.zeroone.wallpaperdeal.ui.theme.Purple80
import com.zeroone.wallpaperdeal.ui.theme.WallpaperViewBackground
import com.zeroone.wallpaperdeal.utils.downloadWallpaper
import com.zeroone.wallpaperdeal.utils.setWallpaper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID



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

    wallpaperId?.let { wallpaperId ->
        var wallpaper by remember { mutableStateOf<Wallpaper?>(null) }
        var checkLike by remember { mutableStateOf<Boolean>(false) }
        var userId by remember { mutableStateOf<String>("") }
        var checkFavorite by remember { mutableStateOf<Boolean>(false) }
        var openReportDialog by remember { mutableStateOf<Boolean>(false) }
        LaunchedEffect(key1 = wallpaperId){
            viewModel.fetchWallpaper(wallpaperId = wallpaperId)
            auth.uid?.let{
                userId = it
                viewModel.checkLike(wallpaperId = wallpaperId, userId = it)
                viewModel.checkFavorites(userId = userId, wallpaperId = wallpaperId)
            }
        }

        var expanded by remember { mutableStateOf<Boolean>(false) }

        checkFavorite = viewModel.checkFavoritesState.value
        Log.e("checkFavorite control in screen",checkFavorite.toString())
        checkLike = viewModel.checkLikeState.value
        wallpaper = viewModel.wallpaperState.value

        wallpaper?.let { wallpaper->
            val currentUserEqualSenderUser = auth.uid.equals(wallpaper.owner?.userId)
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
            Scaffold(
                backgroundColor = WallpaperViewBackground) {
                IconButton(onClick = { navController.navigateUp()},
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
                }
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate("${Screen.OtherProfileScreen.route}/${wallpaper.owner?.userId}") },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = wallpaper.owner?.profilePhoto,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = wallpaper.owner!!.username,
                                style = MaterialTheme.typography.h6,
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                        }
                        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                        AsyncImage(model = wallpaper.imageUrl, contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.66f)
                                .fillMaxSize(0.66f),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.fillMaxHeight(0.075f))
                        Row(modifier = Modifier
                            .fillMaxHeight(0.35f)
                            .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center){
                            wallpaper.description?.let {
                                Text(
                                    text = it,
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 8.dp, end = 10.dp)
                                )
                            }

                        }
                        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                        Text(
                            text = "#${wallpaper.category}",
                            textAlign = TextAlign.Center,
                            color = Purple80,
                            fontSize = 14.sp,
                        )
                        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                        Text(
                            text = "${wallpaper.likeCount} users liked it",
                            color = LikeBlue,
                            fontSize = 14.sp,
                        )
                        Spacer(modifier = Modifier.fillMaxHeight(0.05f))
                        IconButton(onClick = {expanded = true}, modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .fillMaxHeight(0.75f)) {
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
                                }
                            }) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    //Log.e("checkLike Control", checkLike.toString())
                                    when(checkLike){
                                        true -> {Text("Unlike", color = ActiveButton, fontSize = 14.sp)}
                                        false -> {Text("Like", fontSize = 14.sp)}
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
                                    Text("Set as wallpaper", fontSize = 14.sp)
                                }
                            }
                            DropdownMenuItem(onClick = {
                                expanded = false
                                //Log.e("ImageUrl", wallpaper.imageUrl)
                                downloadWallpaper(context, wallpaper.imageUrl)

                            }) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Download wallpaper", fontSize = 14.sp)
                                }
                            }
                            DropdownMenuItem(onClick = {
                                expanded = false
                                CoroutineScope(Dispatchers.Main).launch {
                                    viewModel.addOrRemoveFavorites(wallpaperId = wallpaper.wallpaperId, userId = userId)
                                }
                            }) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when(checkFavorite){
                                        true -> {Text(text = "Remove favorites", color = Color.Red, fontSize = 14.sp )}
                                        false -> {Text(text = "Add favorites", fontSize = 14.sp )}
                                    }
                                }
                            }
                            if(currentUserEqualSenderUser){
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    try{
                                        CoroutineScope(Dispatchers.IO).launch {
                                            viewModel.removeWallpaper(wallpaperId = wallpaperId)
                                            delay(500)
                                        }
                                    }finally {
                                        navController.navigate(Screen.ProfileScreen.route)
                                    }
                                }) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = "Remove Wallpaper", color = Color.Red, fontSize = 14.sp )
                                    }
                                }
                            }
                            DropdownMenuItem(onClick = {
                                expanded = false
                                openReportDialog = true
                            }) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Report", color = Color.Red, fontSize = 14.sp )
                                }
                            }
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize()){
                    if(openReportDialog){
                        ReportDialogScreen(
                            onDismissRequest = { openReportDialog = false },
                            wallpaperReport = Report<Wallpaper>(
                            reportId = UUID.randomUUID().toString(),
                            message = "",
                            payload = wallpaper),
                            cancelDialog = {openReportDialog = false},
                            reportObject = "post" ,
                            userReport = null
                        )
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
        }?: run {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp),
                    color = Color.White
                )
            }
        }
    }
}
