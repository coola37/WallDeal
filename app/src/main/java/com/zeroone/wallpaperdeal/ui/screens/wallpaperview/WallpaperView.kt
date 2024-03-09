package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zeroone.wallpaperdeal.R


@Composable
fun WallpaperViewScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment =  Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.wallpaper_item) , contentDescription = null,
            modifier = Modifier
                //.fillMaxSize()
                //.padding(start = 80.dp, end = 80.dp, top = 120.dp, bottom = 120.dp)
        )

        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .padding(top = 32.dp),
            onClick = { /*TODO*/ }) {
            Icon(
                tint = Color.Unspecified,
                painter = painterResource(id = R.drawable.optionsicon),
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize(),
                contentDescription =null )
        }
    }
}

@Composable
@Preview
fun PreviewWallpaperViewScreen(){
    WallpaperViewScreen()
}