package com.zeroone.wallpaperdeal.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpaper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.WallpaperItemForVerticalStaggeredGrid
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.ActiveButton
import com.zeroone.wallpaperdeal.ui.theme.ProfileButtonColor

@Composable
fun OtherProfileScreen(){
    //Log.e("ProfileScreenWallpaperState", state)
    var checkFollow by remember { mutableStateOf<Boolean>(false) }
    var checkWallDeal by remember { mutableStateOf<Boolean>(false) }
    var wallpapers by remember { mutableStateOf<List<Wallpaper>>(emptyList()) }


    Scaffold(
        backgroundColor= Color.Black,
        bottomBar = {
            BottomNavigationBar(5, null)
        }
    ) {innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Row {
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null, tint = Color.LightGray)
                }
                Text(text = "Username", fontSize = 22.sp, color = Color.LightGray, modifier = Modifier
                    .padding(top = 8.dp, start = 32.dp))
                IconButton(onClick = {}) {
                    Icon(painter = painterResource(id = R.drawable.options_profile), contentDescription = null, tint = Color.LightGray, modifier = Modifier
                        .padding(start = 175.dp))
                }
            }


            Row(modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(id = R.drawable.ic_profile), contentDescription = null,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                        .height(60.dp)
                        .width(60.dp)
                        .clip(CircleShape)
                )

                Column {
                    Row {
                        Text(text = "Followers", fontSize = 16.sp, color = Color.LightGray, modifier = Modifier
                            .padding(top = 24.dp, start = 52.dp))
                        Text(text = "Followed", fontSize = 16.sp, color = Color.LightGray, modifier = Modifier
                            .padding(top = 24.dp, start = 48.dp))
                    }
                    Row {
                        Text(text = "0", fontSize = 24.sp, color = Color.LightGray, modifier = Modifier
                            .padding(top = 4.dp, start = 80.dp))
                        Text(text = "0", fontSize = 24.sp, color = Color.LightGray, modifier = Modifier
                            .padding(top = 4.dp, start = 96.dp))
                    }
                    val buttonColor = ButtonDefaults.buttonColors(ProfileButtonColor)
                    val activeButtonColor =  ButtonDefaults.buttonColors(ActiveButton)
                   Row {
                       when(checkFollow){
                           true -> {
                               Button(onClick = { /*TODO*/ },
                                   colors = activeButtonColor, modifier = Modifier
                                       .padding(top = 8.dp, start = 16.dp)
                                       .width(128.dp)) {

                                   Text(text = "Unfollow", color=Color.LightGray)
                               }
                           }

                           else -> { Button(onClick = { /*TODO*/ },
                               colors = buttonColor, modifier = Modifier
                                   .padding(top = 8.dp, start = 16.dp)
                                   .width(128.dp)) {

                               Text(text = "Follow", color=Color.LightGray)
                           }}
                       }
                       when(checkWallDeal){
                           true ->{
                               Button(onClick = { /*TODO*/ },
                                   colors = activeButtonColor, modifier = Modifier
                                       .padding(top = 8.dp, start = 16.dp)
                                       .width(128.dp)) {
                                   Text(text = "WallDeal", color=Color.LightGray)
                               }
                           }
                           else -> {
                               Button(onClick = { /*TODO*/ },
                                   colors = buttonColor, modifier = Modifier
                                       .padding(top = 8.dp, start = 16.dp)
                                       .width(128.dp)) {
                                   Text(text = "WallDeal", color=Color.LightGray)
                               }
                           }
                       }
                   }
                }
            }
            Column {
                LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                       /* items(state.wallpapers.size){
                            WallpaperItemForVerticalStaggeredGrid(
                                wallpaper = state.wallpapers[it],
                                onClick = {
                                    navController.navigate("${Screen.WallpaperViewScreen.route}/${state.wallpapers[it].wallpaperId}")
                                })
                        }*/
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
    }
}

@Preview
@Composable
fun PreviewOtherProfileScreen(){
    OtherProfileScreen()
}