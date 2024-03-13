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
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.WallpaperItemForVerticalStaggeredGrid
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.ProfileButtonColor


@Composable
fun ProfileScreen(
    navController: NavController,
    auth: FirebaseAuth,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.value


    //Log.e("ProfileScreenWallpaperState", state)
    Scaffold(
        backgroundColor= Color.Black,
        bottomBar = {
            BottomNavigationBar(4, navController)
        }
    ) {innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {

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
                            .padding(top = 24.dp, start = 32.dp))
                    }
                    Row {
                        Text(text = "0", fontSize = 24.sp, color = Color.LightGray, modifier = Modifier
                            .padding(top = 4.dp, start = 80.dp))
                        Text(text = "0", fontSize = 24.sp, color = Color.LightGray, modifier = Modifier
                            .padding(top = 4.dp, start = 80.dp))
                    }
                }
                IconButton(
                    onClick = { auth.signOut() },
                    modifier = Modifier.padding(start = 48.dp)
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_setting), contentDescription =null )
                }

            }
            val color = ButtonDefaults.buttonColors(ProfileButtonColor)
            Row {
                Text(text = "Username", fontSize = 20.sp, color = Color.LightGray, modifier = Modifier
                    .padding(top = 16.dp, start = 32.dp))
                Button(
                    onClick = { /*TODO*/ },
                    colors = color, modifier = Modifier.padding(start = 72.dp, top = 4.dp)
                    ) {
                    Text(text = "Edit Profile", fontSize = 16.sp,color = Color.LightGray, modifier = Modifier
                        )
                }
            }
            Column {
                LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(state.wallpapers.size){
                            WallpaperItemForVerticalStaggeredGrid(
                                wallpaper = state.wallpapers[it],
                                onClick = {
                                    navController.navigate("${Screen.WallpaperViewScreen.route}/${state.wallpapers[it].wallpaperId}")
                                })
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
    }
}

/*
@Composable
@Preview
fun PreviewProfileScreen(){
    ProfileScreen()
}*/
