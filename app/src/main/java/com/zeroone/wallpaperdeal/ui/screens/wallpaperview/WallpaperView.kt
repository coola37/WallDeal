package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zeroone.wallpaperdeal.R


@Composable
fun WallpaperViewScreen(
    navController: NavController,
    viewModel: WallpaperViewViewModel = hiltViewModel()){
    val context = LocalContext.current
    val wallpaperId = navController.currentBackStackEntry?.arguments?.getString("wallpaperId")
    wallpaperId?.let {
        viewModel.fetchWallpaper(wallpaperId = wallpaperId!!)
        var wallpaper = viewModel.wallpaperState.value
        wallpaper?.let {

                Box(modifier = Modifier.fillMaxSize()){
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),

                        horizontalAlignment =  Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box( modifier = Modifier
                            //.fillMaxSize()
                            .padding(top = 40.dp, bottom = 24.dp)
                            .width(400.dp)
                            .height(500.dp)
                        ) {
                            AsyncImage(model = wallpaper?.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize()
                            )
                        }

                        Row {
                            IconButton(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .padding(top = 16.dp),
                                onClick = { /*TODO*/ }) {
                                Icon(
                                    tint = Color.Unspecified,
                                    painter = painterResource(id = R.drawable.empty_heart),
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .fillMaxSize(),
                                    contentDescription =null
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Likes: 0", color = Color.LightGray, fontSize = 24.sp,
                                modifier = Modifier.padding(top= 24.dp)
                            )

                        }

                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .padding(top = 18.dp)
                                .width(60.dp)
                                .height(50.dp),
                            onClick = { /*TODO*/ }) {
                            Icon(
                                tint = Color.Unspecified,
                                painter = painterResource(id = R.drawable.ic_download),
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .fillMaxSize(),
                                contentDescription =null )
                        }
                    }
                }
            }
        }
}

/*
@Composable
@Preview
fun PreviewWallpaperViewScreen(){
    WallpaperViewScreen()
}*/
