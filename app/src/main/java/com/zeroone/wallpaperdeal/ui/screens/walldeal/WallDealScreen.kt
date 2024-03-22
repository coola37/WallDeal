package com.zeroone.wallpaperdeal.ui.screens.walldeal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.theme.TextFieldBaseColor

@Composable
fun WallDealScreen(){
    Scaffold(
        bottomBar = { BottomNavigationBar(selectedItem = 3, navController = null)},
        backgroundColor = Color.Black
    ) {
        Column(
            modifier = Modifier.padding(it),
        ) {
            Column {

            }
            Row {
                var text by remember { mutableStateOf("")}
                val textFieldColor = TextFieldDefaults.textFieldColors(
                    backgroundColor = TextFieldBaseColor,
                    textColor = Color.LightGray,
                    disabledTextColor = Color.White,
                    unfocusedLabelColor = Color.Gray,

                )
                TextField(
                    value = text, onValueChange = { text = it },
                    colors = textFieldColor,
                    label = { Text(text = "Message", fontSize = 18.sp )}
                )
            }
        }
    }
}

@Composable
fun chatItem(wallpaper: Wallpaper){
    Column{
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            AsyncImage(
                model = wallpaper.owner!!.userDetail?.profilePhoto,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp)
            )
            Text(text = wallpaper.owner!!.username, color = Color.LightGray, fontSize = 20.sp, modifier = Modifier.padding(top = 16.dp, start = 16.dp))
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .height(600.dp)
        ) {
            AsyncImage(
                model = wallpaper.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.66f)
                    .fillMaxSize(0.75f),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            wallpaper.description?.let {
                Text(
                    text = it, fontSize = 20.sp,
                    color = Color.LightGray, textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Row {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.make_walldeal),
                        contentDescription = null,
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.width(64.dp))
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(60.dp)
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
@Preview
@Composable
fun PreviewWallDealScreen(){
    WallDealScreen()
}