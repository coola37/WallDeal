package com.zeroone.wallpaperdeal.ui.screens.profile


import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.model.User
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.WallpaperItemForVerticalStaggeredGrid
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.ProfileButtonColor
import com.zeroone.wallpaperdeal.ui.theme.YellowWallDeal
import kotlinx.coroutines.Dispatchers


@Composable
fun ProfileScreen(
    navController: NavController,
    auth: FirebaseAuth,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    auth.uid?.let { currentUserId ->
        var selected by remember { mutableStateOf<String>("grid") }
        var wallpapers by remember { mutableStateOf<List<Wallpaper?>>(emptyList()) }
        var currentUser by remember { mutableStateOf<User?>(null) }
        LaunchedEffect(Dispatchers.IO){
            viewModel.fetchItems(currentUserId)
        }
        currentUser = viewModel.stateItems.value.user
        Log.e("Wallpaper Control", viewModel.stateItems.value.user.toString())

        Scaffold(
            backgroundColor= Color.Black,
            bottomBar = {
                BottomNavigationBar(4, navController)
            }
        ) {innerPadding ->
            currentUser?.let{ currentUser ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Row {
                        Text(
                            text = currentUser.username,
                            fontSize = 22.sp,
                            color = Color.White,
                            modifier = Modifier
                                .padding(top = 8.dp, start = 24.dp)
                        )
                        IconButton(
                            onClick = { auth.signOut() },
                            modifier = Modifier.padding(start = 240.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_setting),
                                contentDescription = null
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = currentUser.userDetail?.profilePhoto,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(top = 16.dp, start = 16.dp)
                                .size(60.dp)
                                .clip(CircleShape)
                        )
                        Column {
                            Row {
                                Text(
                                    text = "Followers",
                                    fontSize = 16.sp,
                                    color = Color.LightGray,
                                    modifier = Modifier
                                        .padding(top = 12.dp, start = 64.dp)
                                )
                                Text(
                                    text = "Followed",
                                    fontSize = 16.sp,
                                    color = Color.LightGray,
                                    modifier = Modifier
                                        .padding(top = 12.dp, start = 32.dp)
                                )
                            }
                            Row {
                                currentUser.userDetail?.followers?.let {
                                    Text(
                                        text = "${it.size}",
                                        fontSize = 24.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .padding(top = 4.dp, start = 92.dp)
                                    )
                                } ?: run {
                                    Text(
                                        text = "0",
                                        fontSize = 24.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .padding(top = 4.dp, start = 92.dp)
                                    )
                                }
                                currentUser.userDetail?.followed?.let {
                                    Text(
                                        text = "${it.size}",
                                        fontSize = 24.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .padding(top = 4.dp, start = 80.dp)
                                    )
                                } ?: run {
                                    Text(
                                        text = "0",
                                        fontSize = 24.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .padding(top = 4.dp, start = 80.dp)
                                    )
                                }
                            }
                        }
                    }
                    val color = ButtonDefaults.buttonColors(ProfileButtonColor)
                    Row {
                        Button(
                            onClick = { /*TODO*/ },
                            colors = color, modifier = Modifier
                                .padding(start = 140.dp, top = 4.dp)
                                .width(170.dp)
                        ) {
                            Text(
                                text = "Edit Profile",
                                fontSize = 16.sp,
                                color = Color.LightGray,
                                modifier = Modifier
                            )
                        }
                    }
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        IconButton(modifier = Modifier
                            .padding(start = 72.dp),
                            onClick = { selected = "grid" }) {
                            Icon(
                                imageVector = Icons.Default.GridOn,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(35.dp)
                            )
                        }
                        IconButton(modifier = Modifier
                            .padding(start = 124.dp),
                            onClick = { selected = "favorite" }) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(35.dp)
                            )
                        }
                    }
                    when (selected) {
                        "grid" -> {
                            wallpapers = viewModel.stateItems.value.wallpapers
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 0.dp, top = 4.dp)
                            ) {
                                val start = Offset(0f, size.height / 2)
                                val end = Offset(540f, size.height / 2)
                                drawLine(
                                    start = start,
                                    end = end,
                                    color = Color.LightGray,
                                    strokeWidth = 5f,
                                    cap = StrokeCap.Round
                                )
                            }
                        }

                        "favorite" -> {
                            wallpapers =
                                viewModel.stateItems.value.user!!.userDetail!!.favoriteWallpapers
                            Canvas(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 200.dp, top = 4.dp)
                            ) {
                                val start = Offset(0f, size.height / 2)
                                val end = Offset(540f, size.height / 2)
                                drawLine(
                                    start = start,
                                    end = end,
                                    color = Color.LightGray,
                                    strokeWidth = 5f,
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }
                    Column {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            verticalItemSpacing = 4.dp,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            content = {
                                wallpapers?.let { wallpaperList ->
                                    items(wallpapers.size) {
                                        WallpaperItemForVerticalStaggeredGrid(
                                            wallpaper = wallpaperList[it]!!,
                                            onClick = {
                                                navController
                                                    .navigate("${Screen.WallpaperViewScreen.route}/${wallpaperList[it]?.wallpaperId}")
                                            }
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            } ?: run {
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
}