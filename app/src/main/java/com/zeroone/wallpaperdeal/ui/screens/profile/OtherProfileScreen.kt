package com.zeroone.wallpaperdeal.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.zeroone.wallpaperdeal.ui.theme.ActiveButton
import com.zeroone.wallpaperdeal.ui.theme.ProfileButtonColor
import kotlinx.coroutines.Dispatchers

@Composable
fun OtherProfileScreen(
    auth: FirebaseAuth,
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
){
    //Log.e("ProfileScreenWallpaperState", state)
    val userId: String? =  navController.currentBackStackEntry?.arguments?.getString("userId")
    var checkFollow by remember { mutableStateOf<Boolean>(false) }
    var checkWallDeal by remember { mutableStateOf<Boolean>(false) }
    var wallpapers by remember { mutableStateOf<List<Wallpaper>>(emptyList())}
    var user by remember { mutableStateOf<User?>(null) }
    var wallDealRequestState by remember { mutableStateOf<Boolean>(false) }
    var wallDealState by remember { mutableStateOf<Boolean>(false) }
    var wallDealForBetweenCurrentUserToTargetUserState by remember { mutableStateOf<Boolean>(false) }


    userId?.let{
        LaunchedEffect(key1 = userId) {
            viewModel.fetchItems(userId = it)
        }
        wallpapers = viewModel.stateItems.value.wallpapers
        user = viewModel.stateItems.value.user
        Log.e("current - target", auth.uid!! + "-" + userId)
        user?.let { user ->

            LaunchedEffect(Dispatchers.IO){
                viewModel.checkWallDeal(targetUserId = user.userId)
                viewModel.checkWallDealRequest(currentUserId = auth.uid!!, targetUserId = user.userId)
                viewModel.checkWallDealForBetweenUserToUser(currentUserId = auth.uid!!, targetUserId =  user.userId)
            }
            wallDealRequestState = viewModel.wallDealRequestState.value
            wallDealState = viewModel.wallDealForTargetUserState.value
            wallDealForBetweenCurrentUserToTargetUserState = viewModel.wallDealForBetweenUserToUserState.value

            if (userId == auth.uid) {
                ProfileScreen(navController = navController, auth = auth)
            } else {
                Scaffold(
                    backgroundColor = Color.Black,
                    bottomBar = {
                        BottomNavigationBar(1, navController)
                    }
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Row {
                            IconButton(onClick = {}) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_back),
                                    contentDescription = null,
                                    tint = Color.LightGray
                                )
                            }
                            Text(
                                text = user.username,
                                fontSize = 18.sp,
                                color = Color.LightGray,
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            )
                            Spacer(modifier = Modifier.fillMaxWidth(0.85f))
                            IconButton(onClick = {}) {
                                Icon(
                                    painter = painterResource(id = R.drawable.options_profile),
                                    contentDescription = null,
                                    tint = Color.LightGray,
                                    modifier = Modifier
                                )
                            }
                        }


                        Row(modifier = Modifier.fillMaxWidth()) {
                            AsyncImage(
                                model = user.userDetail?.profilePhoto,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 16.dp, start = 16.dp)
                                    .height(60.dp)
                                    .width(60.dp)
                                    .clip(CircleShape)
                            )

                            Column {
                                Row {
                                    Text(
                                        text = "Followers",
                                        fontSize = 14.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .padding(top = 24.dp, start = 52.dp)
                                    )
                                    Text(
                                        text = "Followed",
                                        fontSize = 14.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .padding(top = 24.dp, start = 48.dp)
                                    )
                                }
                                Row {
                                    Text(
                                        text = user.userDetail?.followers?.size.toString(),
                                        fontSize = 16.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .padding(top = 4.dp, start = 80.dp)
                                    )
                                    Text(
                                        text = user.userDetail?.followed?.size.toString(),
                                        fontSize = 16.sp,
                                        color = Color.LightGray,
                                        modifier = Modifier
                                            .padding(top = 4.dp, start = 96.dp)
                                    )
                                }
                                val buttonColor = ButtonDefaults.buttonColors(ProfileButtonColor)
                                val activeButtonColor = ButtonDefaults.buttonColors(ActiveButton)
                                Row {
                                    when (checkFollow) {
                                        true -> {
                                            Button(
                                                onClick = { /*TODO*/ },
                                                colors = activeButtonColor, modifier = Modifier
                                                    .padding(top = 8.dp)
                                                    .width(128.dp)
                                            ) {

                                                Text(text = "Unfollow", color = Color.LightGray)
                                            }
                                        }

                                        else -> {
                                            Button(
                                                onClick = { /*TODO*/ },
                                                colors = buttonColor, modifier = Modifier
                                                    .padding(top = 8.dp)
                                                    .width(128.dp)
                                            ) {

                                                Text(text = "Follow", color = Color.LightGray)
                                            }
                                        }
                                    }
                                    when (wallDealRequestState) {
                                        true -> {
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Button(
                                                onClick = { /*TODO*/ },
                                                colors = activeButtonColor, modifier = Modifier
                                                    .padding(top = 8.dp)
                                                    .width(128.dp)
                                            ) {
                                                Text(text = "Request sent", color = Color.LightGray)
                                            }
                                        }
                                        else -> {
                                            when(wallDealState){
                                                false -> {
                                                    Spacer(modifier = Modifier.width(16.dp))
                                                    Button(
                                                        onClick = {
                                                            viewModel.sendWallDealRequest(
                                                                senderUserId = auth.uid!!,
                                                                receiverUserId = userId
                                                            ) },
                                                        colors = buttonColor, modifier = Modifier
                                                            .padding(top = 8.dp)
                                                    ) {
                                                        Text(text = "WallDeal", color = Color.LightGray)
                                                    }
                                                }
                                                true -> {
                                                    when(wallDealForBetweenCurrentUserToTargetUserState){
                                                        true -> {
                                                            Spacer(modifier = Modifier.width(16.dp))
                                                            Button(
                                                                onClick = { /*TODO*/ },
                                                                colors = buttonColor, modifier = Modifier
                                                                    .padding(
                                                                        top = 8.dp,
                                                                    )

                                                            ) {
                                                                Text(text = "Cancel WallDeal", color = Color.LightGray, fontSize = 9.sp,
                                                                    maxLines = 1)
                                                            }
                                                        }
                                                        false -> {
                                                            Log.d("User has a WallDeal with other users", "Yes")
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Column {
                            LazyVerticalStaggeredGrid(
                                columns = StaggeredGridCells.Fixed(2),
                                verticalItemSpacing = 4.dp,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                content = {
                                    items(wallpapers.size){
                                        WallpaperItemForVerticalStaggeredGrid(
                                            wallpaper = wallpapers[it],
                                            onClick = {
                                                navController.navigate("${Screen.WallpaperViewScreen.route}/${wallpapers[it].wallpaperId}")
                                            })
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
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