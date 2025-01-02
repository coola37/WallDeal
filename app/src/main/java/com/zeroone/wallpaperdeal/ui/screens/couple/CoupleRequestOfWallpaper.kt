package com.zeroone.wallpaperdeal.ui.screens.couple

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.zeroone.wallpaperdeal.data.model.WallpaperRequest
import com.zeroone.wallpaperdeal.ui.theme.DeleteColor
import com.zeroone.wallpaperdeal.ui.theme.ProfileButtonColor
import com.zeroone.wallpaperdeal.utils.setWallpaper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WallDealRequestOfWallpaper(request: WallpaperRequest, viewModel: CoupleOprtViewModel, userId: String) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    LaunchedEffect(key1 = request.imageUrl){
        val imageRequest = ImageRequest.Builder(context)
            .data(request.imageUrl)
            .build()

        val result = context.imageLoader.execute(imageRequest)
        imageBitmap = if (result.drawable is BitmapDrawable) {
            (result.drawable as BitmapDrawable).bitmap
        } else {
            null
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxHeight(0.1f)) {
            AsyncImage(
                model = request.senderUser.profilePhoto,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp)
            )
            Text(
                text = request.senderUser.username,
                color = Color.LightGray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp, start = 8.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.fillMaxHeight(1f)
        ) {
            AsyncImage(
                model = request.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.66f)
                    .fillMaxSize(0.75f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            request.message?.let {
                Text(
                    text = it,
                    fontSize = 16.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp)
                )
                Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (userId != request.senderUser.userId) {
                    Button(
                        colors = ButtonDefaults.buttonColors(ProfileButtonColor),
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch{
                                try{
                                    setWallpaper(context = context, bitmap = imageBitmap!!)
                                }finally {
                                    viewModel.cancelPost(userId = userId, request = request)
                                }
                            }
                            },
                        modifier = Modifier,
                        shape = CircleShape
                    ) {
                        Text(text = "Set as wallpaper", color = Color.LightGray, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.fillMaxWidth(0.15f))
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(DeleteColor),
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.cancelPost(userId = userId, request = request)
                            }
                            Log.e("click", "true")
                        }  ,
                        modifier = Modifier,
                        shape = CircleShape
                    ) {
                    Text(text = "Delete request", color = Color.LightGray, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.fillMaxHeight(1f))
            }
        }
    }
}