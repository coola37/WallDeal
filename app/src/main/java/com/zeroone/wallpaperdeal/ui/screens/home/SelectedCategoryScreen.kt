package com.zeroone.wallpaperdeal.ui.screens.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.WallpaperListVerticalStaggeredGrid
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.TopAppBarColor
import kotlinx.coroutines.Dispatchers

@Composable
fun SelectedCategoryScreen(
    navController: NavController,
    auth: FirebaseAuth,
    viewModel: HomeViewModel = hiltViewModel()
){
    val categoryName = navController.currentBackStackEntry?.arguments?.getString("category")
    val scrollState = LazyStaggeredGridState()
    var isTopAppBarVisible by remember { mutableStateOf(true) }
    categoryName?.let {

        LaunchedEffect(Dispatchers.IO){
            viewModel.getWallpapersFromLocal()
        }
        val state = viewModel.state.value
        Scaffold(
            topBar = {
                if (isTopAppBarVisible) {
                    TopAppBar(
                        backgroundColor = TopAppBarColor,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        Row(modifier = Modifier.fillMaxWidth()) {
                            IconButton(onClick = { navController.navigateUp()},
                                modifier = Modifier.fillMaxWidth(0.1f)
                            ) {
                                Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
                            }
                            Spacer(modifier = Modifier.fillMaxWidth(0.125f))
                            Image(
                                painter = painterResource(id = R.drawable.walldeallogo),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxHeight(0.75f)
                                    .fillMaxWidth(0.75f)

                            )
                        }
                    }
                }
            },
            bottomBar = { BottomNavigationBar(0, navController) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(Color.Black)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()){
                    Text(categoryName!!, fontSize = 18.sp, color = Color.LightGray)
                }
                val wallpaperList = state.wallpapers.filter { it.category == categoryName }
                WallpaperListVerticalStaggeredGrid(
                    list = wallpaperList ,
                    scrollState = scrollState,
                    onItemClick = {
                        navController.navigate("${Screen.WallpaperViewScreen.route}/${it.wallpaperId}")
                    },
                    onTopAppBarVisibilityChanged = {
                        isTopAppBarVisible = it
                        isTopAppBarVisible = it
                    }
                )
            }
        }
    }
}