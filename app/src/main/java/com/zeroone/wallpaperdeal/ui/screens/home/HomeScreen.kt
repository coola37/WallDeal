package com.zeroone.wallpaperdeal.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.theme.TopAppBarColor
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.WallpaperListVerticalStaggeredGrid
import com.zeroone.wallpaperdeal.ui.screens.Screen

@Composable
fun HomeScreen(navController: NavController, auth: FirebaseAuth, viewModel: HomeViewModel = hiltViewModel()) {
    val scrollState = LazyStaggeredGridState()
    var isTopAppBarVisible by remember { mutableStateOf(true) }
    var wallpapers by remember { mutableStateOf<List<Wallpaper>>(emptyList()) }
    var requestsState by remember { mutableStateOf(false) }
    val state = viewModel.state.value
    wallpapers = state.wallpapers
    auth.uid?.let{
        LaunchedEffect(key1 = it) {
            viewModel.getWallpapersFromLocal()
            viewModel.checkRequestForUser(it)
        }
    }
    requestsState = viewModel.requestsState.value

    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            if (isTopAppBarVisible) {
                TopAppBar(
                    backgroundColor = TopAppBarColor,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.fillMaxWidth(0.35f))
                        Image(
                            painter = painterResource(id = R.drawable.walldeallogo),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.50f)
                                .fillMaxHeight(0.75f)
                        )
                        Spacer(modifier = Modifier.fillMaxWidth(0.6f))
                        IconButton(onClick = { navController.navigate(Screen.RequestsScreen.route)},
                            modifier = Modifier
                                .fillMaxSize(0.5f)
                                .fillMaxWidth(0.1f),
                            enabled = requestsState
                            ) {
                            when(requestsState){
                                true -> {
                                    Image(painter = painterResource(id = R.drawable.notification_active), contentDescription = null,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                false -> {
                                    Image(painter = painterResource(id = R.drawable.notification_passive), contentDescription = null,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(0, navController)
        }
    ) { innerPadding ->
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)) {
                TextButton(onClick = {  Log.d("HomeScreenControl", "Now HomeScreen")}) {
                    Text(text = "Home", color = Color.White, fontSize = 14.sp,)
                }
                Spacer(modifier = Modifier.width(30.dp))
                TextButton(onClick = {navController.navigate(Screen.HomeCategoryScreen.route)}) {
                    Text(text = "Categories", color = Color.Gray, fontSize = 14.sp)
                }
            }
            if(wallpapers.isNotEmpty()){
                WallpaperListVerticalStaggeredGrid(
                    list = wallpapers,
                    scrollState = scrollState,
                    onItemClick = {
                        navController.navigate("${Screen.WallpaperViewScreen.route}/${it.wallpaperId}")
                    },
                    onTopAppBarVisibilityChanged = {
                        isTopAppBarVisible = it
                        isTopAppBarVisible = it
                    }

                )
            }else{
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
