package com.zeroone.wallpaperdeal.ui.screens.home

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.model.Category
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.TopAppBarColor
import com.zeroone.wallpaperdeal.utils.ListCategory

@Composable
fun HomeCategoryScreen(navController: NavController){
    val scrollState = rememberLazyListState()
    var isTopAppBarVisible by remember { mutableStateOf(true) }
    Scaffold(
        backgroundColor = Color.Black,
        topBar = {
            if (isTopAppBarVisible) {
                TopAppBar(
                    backgroundColor = TopAppBarColor,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.walldeallogo),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(0.75f)
                        )
                    }
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
            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)) {
                TextButton(onClick = {navController.navigate(Screen.HomeScreen.route)
                    }) {
                    Text(text = "Home", color = Color.Gray, fontSize = 14.sp,)
                }
                Spacer(modifier = Modifier.width(30.dp))
                TextButton(onClick = {Log.d("HomeScreenControl", "Now Category")}) {
                    Text(text = "Categories", color = Color.White, fontSize = 14.sp)
                }
            }
            StaggeredLazyGrid(categoryList = ListCategory.list, navController)
        }
    }
}

@Composable
fun WallpaperItem(category: Category, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(modifier = modifier.clickable(onClick = onClick)
       ) {
        AsyncImage(
            model = category.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .aspectRatio(1f) // Kare görüntüleri göstermek için en-boy oranını 1:1 olarak ayarlar
                .padding(1.dp) // Fotoğraflar arasında boşluk bırakır // Hücrenin tüm alanını kaplar

        )
    }
}

@Composable
fun StaggeredLazyGrid(categoryList :List<Category>, navController: NavController){
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(categoryList.size) { index ->
                WallpaperItem(category = categoryList[index], onClick = {
                    navController.navigate("${Screen.SelectedCategoryScreen.route}/${categoryList[index].categoryName}")
                })
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}


/*
@Preview
@Composable
fun PreviewHomeCategoryScreen(){
    HomeCategoryScreen()
}*/
