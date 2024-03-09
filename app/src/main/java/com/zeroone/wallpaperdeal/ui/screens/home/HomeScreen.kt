package com.zeroone.wallpaperdeal.ui.screens.home

import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.TopAppbarText
import com.zeroone.wallpaperdeal.ui.theme.TopAppBarColor
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material.Text
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.WallpaperListVerticalStaggeredGrid
import com.zeroone.wallpaperdeal.ui.theme.ThemeGray


@Composable
fun HomeScreen(navController: NavController, auth: FirebaseAuth, viewModel: HomeViewModel = hiltViewModel()) {
    val scrollState = LazyStaggeredGridState()
    var isTopAppBarVisible by remember { mutableStateOf(true) }
    var isTop10ListVisible by remember { mutableStateOf(true) }
    val state = viewModel.state.value

    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            if (isTopAppBarVisible) {
                TopAppBar(
                    backgroundColor = TopAppBarColor,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.walldeallogo),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 110.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        },
        bottomBar = {
            BottomNavigationBar(0, navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TopAppbarText(navController = navController)
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 70.dp)) {
                val start = Offset(0f, size.height / 2)
                val end = Offset(250f, size.height / 2)
                drawLine(
                    start = start,
                    end = end,
                    color = ThemeGray,
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
            }

           /* if (isTop10ListVisible) {
                Text(text = "Populer Collection Top 10",fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(all = 8.dp))
            }else{ Log.d("istop10listVisible", isTop10ListVisible.toString())}*/

            val listItem = R.drawable.wallpaper_item

            //Top10WallpaperList(photos = null, modifier = Modifier, isVisible = isTop10ListVisible)
            if (isTop10ListVisible) {
                Text( text = "Popular New Wallpapers",fontSize = 20.sp, color = Color.White, modifier = Modifier.padding(all = 8.dp)
                )
            }else{ Log.d("istop10listVisible", isTop10ListVisible.toString())}
            WallpaperListVerticalStaggeredGrid(list = state.wallpapers, scrollState = scrollState){
                isTopAppBarVisible = it
                isTopAppBarVisible = it
            }
        }
    }
}

@Composable
fun Top10WallpaperList(photos: List<Int>, modifier: Modifier = Modifier, isVisible: Boolean = true) {
    if (isVisible) {
        LazyRow(modifier = modifier) {
            /*items(photos) { photo ->
                PhotoItem(photoRes = photo, modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(120.dp))
            }*/
        }
    }
}



/*
@Composable
@Preview
fun previewScreen(@PreviewParameter navController: NavController){
    HomeScreen(navController)
}

*/
