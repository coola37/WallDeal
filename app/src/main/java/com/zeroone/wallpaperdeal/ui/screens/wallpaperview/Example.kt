package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.theme.WallpaperViewBackground

@Composable
fun MyScreen() {
    Scaffold(backgroundColor = WallpaperViewBackground) {
       IconButton(onClick = { /*TODO*/ },
           modifier = Modifier.padding(start = 8.dp, top = 8.dp)
       ) {
           Image(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
       }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = "",
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "username",
                    style = MaterialTheme.typography.h6,
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            /*Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.66f)
                    .fillMaxSize(0.66f),
                contentScale = ContentScale.Crop
            )*/
            val url ="https://firebasestorage.googleapis.com/v0/b/wall-deal.appspot.com/o/wallpapers%2Fde71d038-a5f1-4dbd-b1ec-a46d318f1224?alt=media&token=ce76eb54-0880-4165-8e5f-7ed3a6e54992"
            AsyncImage(model = url, contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.66f)
                    .fillMaxSize(0.66f),
                contentScale = ContentScale.Crop
                )
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Description",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 0.dp, bottom = 8.dp)
            )

            Text(
                text = "20 users liked it",
                color = Color.LightGray,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 0.dp)
            )
            Spacer(modifier = Modifier.height(36.dp))
            IconButton(onClick = {}) {
                if(true){
                    androidx.compose.material3.Icon(
                        tint = Color.Unspecified,
                        painter = painterResource(id = R.drawable.ic_cancel),
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxSize(),
                        contentDescription = null
                    )

                }else{
                    androidx.compose.material3.Icon(
                        tint = Color.Unspecified,
                        painter = painterResource(id = R.drawable.options_icon),
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxSize(),
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MyScreenPreview() {
    MyScreen(

    )
}

/*
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
                    Row() {
                        AsyncImage(
                            model = wallpaper.owner?.profilePhoto,
                            contentDescription = null,
                            modifier = Modifier
                                .width(10.dp)
                                .height(30.dp)
                                .padding(top = 16.dp)
                                .clip(CircleShape)
                        )
                        TextButton(
                            onClick = { /*TODO*/ }, modifier = Modifier
                                .padding(start = 0.dp)
                        ) {
                            Text(
                                text = wallpaper.owner?.username!!,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                    //Spacer(modifier = Modifier.height(650.dp))
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
 */