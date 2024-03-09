package com.zeroone.wallpaperdeal.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter

import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.ThemeGray
import com.zeroone.wallpaperdeal.utils.BlurHashDecoder

@Composable
fun TopAppbarText(navController: NavController){
    Row(modifier = Modifier
        .padding(top = 0.dp)
        .fillMaxWidth()
        .background(Color.Black)) {
        val currentDestination = navController.currentDestination?.route
        TextButton(onClick = { if(currentDestination != Screen.HomeScreen.route){
            navController.navigate(Screen.HomeScreen.route)
        }else{
            Log.d("HomeScreenControl", "Now HomeScreen")
        }
        }) {
            Text(text = "Home", color = Color.White, fontSize = 16.sp,
                modifier = Modifier.padding(start = 85.dp))
        }
        TextButton(onClick = {if(currentDestination != Screen.HomeCategoryScreen.route){
            navController.navigate(Screen.HomeCategoryScreen.route)
        }else{
            Log.d("HomeCategoryScreenControl", "Now HomeCategoryScreen")
        }}) {
            Text(text = "Categories", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(start = 50.dp))
        }
    }
}

@Composable
fun BlurHashImage(context: Context, wallpaper: Wallpaper) {
    val resources = context.resources
    val wallpaper = remember { wallpaper }
    val bitmapDrawable = remember {
        BlurHashDecoder.blurHashBitmap(resources, wallpaper)
    }
    val painter = remember {
        BitmapPainter(bitmapDrawable.bitmap.asImageBitmap())
    }

    Image(
        painter = painter,
        contentDescription = null
    )
}

@Composable
fun BottomNavigationBar(selectedItem: Int, navController: NavController?) {
    var selectedItem by remember { mutableStateOf(selectedItem) }

    BottomAppBar(backgroundColor = Color.Black) {
        BottomNavigation {
            val items = listOf(
                BottomNavItem("Home", R.drawable.ic_home, 0, Screen.HomeScreen.route),
                BottomNavItem("Search", R.drawable.ic_search, 1, Screen.HomeScreen.route),
                BottomNavItem("Share", R.drawable.ic_share, 2, Screen.ShareScreen.route),
                BottomNavItem("Group", R.drawable.ic_group, 3, Screen.HomeScreen.route),
                BottomNavItem("Profile", R.drawable.ic_profile, 4, Screen.ProfileScreen.route)
            )

            items.forEach { item ->
                BottomNavigationItem(
                    selected = selectedItem == item.index,
                    onClick = { navController?.navigate(item.route)},
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.label
                        )
                    },
                    selectedContentColor = ThemeGray, // Seçili öğenin rengini buradan ayarlayabilirsiniz
                    unselectedContentColor = Color.White, // Seçilmemiş öğelerin rengi
                    modifier = Modifier.background(Color.Black),
                )
            }
        }
    }
}

data class BottomNavItem(val label: String, val icon: Int, val index: Int, val route: String)


@Composable
fun WallpaperItemForVerticalStaggeredGrid(wallpaper: Wallpaper) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(model = wallpaper.imageUrl, contentDescription = null)
    }
}

@Composable
fun WallpaperListVerticalStaggeredGrid(list: List<Wallpaper>, scrollState: LazyStaggeredGridState,
                                       onTopAppBarVisibilityChanged: (Boolean) -> Unit){
    Column {
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = scrollState,
            content = {
                items(list.size){
                    WallpaperItemForVerticalStaggeredGrid(wallpaper = list[it])
                }

                scrollState.run {
                    val firstVisibleItemIndex = firstVisibleItemIndex
                    val firstItemScrollOffset = firstVisibleItemScrollOffset
                    onTopAppBarVisibilityChanged(firstVisibleItemIndex == 0 && firstItemScrollOffset == 0)
                }},
            modifier = Modifier.fillMaxSize()
        )
    }
}