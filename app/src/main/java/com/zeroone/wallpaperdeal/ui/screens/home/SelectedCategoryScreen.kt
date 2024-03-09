package com.zeroone.wallpaperdeal.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.WallpaperListVerticalStaggeredGrid
import com.zeroone.wallpaperdeal.ui.theme.TopAppBarColor

@Composable
fun SelectedCategoryScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
){
    val categoryName = navController.currentBackStackEntry?.arguments?.getString("category")
    val scrollState = LazyStaggeredGridState()
    var isTopAppBarVisible by remember { mutableStateOf(true) }
    val state = viewModel.state.value
    //var isTop10ListVisible by remember { mutableStateOf(true) }
    categoryName?.let {

        Scaffold(
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
            bottomBar = { BottomNavigationBar(0, null) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color.Black)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()){
                    Text(categoryName!!, fontSize = 24.sp, color = Color.LightGray)
                }
                val wallpaperList = state.wallpapers.filter { it.category == categoryName }
                WallpaperListVerticalStaggeredGrid(
                    list = wallpaperList ,
                    scrollState = scrollState,
                    onTopAppBarVisibilityChanged = {
                        isTopAppBarVisible = it
                        isTopAppBarVisible = it
                    }
                )
            }
        }
    }
}

/*
@Composable
@Preview
fun PreviewSelectedCategoryScreen(){
    SelectedCategoryScreen()
}*/
