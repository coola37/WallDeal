package com.zeroone.wallpaperdeal.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.YellowWallDeal

@Composable
fun TopAppbarText(navController: NavController, text1: String, text2: String){

}


@Composable
fun BottomNavigationBar(selectedItem: Int, navController: NavController?) {
    val selectedItem by remember { mutableStateOf(selectedItem) }

    BottomAppBar(backgroundColor = Color.Black) {
        BottomNavigation {
            val items = listOf(
                BottomNavItem("Home", R.drawable.ic_home, 0, Screen.HomeScreen.route),
                BottomNavItem("Search", R.drawable.ic_search, 1, Screen.SearchScreen.route),
                BottomNavItem("Share", R.drawable.ic_share, 2, Screen.ShareScreen.route),
                BottomNavItem("Group", R.drawable.ic_group, 3, Screen.WallDealScreen.route),
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
                    selectedContentColor = Color.White, // Seçili öğenin rengini buradan ayarlayabilirsiniz
                    unselectedContentColor = Color.Gray, // Seçilmemiş öğelerin rengi
                    modifier = Modifier.background(Color.Black),
                )
            }
        }
    }
}

data class BottomNavItem(val label: String, val icon: Int, val index: Int, val route: String)


@Composable
fun WallpaperItemForVerticalStaggeredGrid(
    wallpaper: Wallpaper,
    onClick: () -> Unit // onClick lambda parameter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 6.dp)
            .clickable(onClick = onClick), // Add clickable modifier with onClick lambda
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(model = wallpaper.imageUrl, contentDescription = null)
    }
}

@Composable
fun WallpaperListVerticalStaggeredGrid(
    list: List<Wallpaper>,
    scrollState: LazyStaggeredGridState,
    onTopAppBarVisibilityChanged: (Boolean) -> Unit,
    onItemClick: (Wallpaper) -> Unit // onItemClick lambda parameter
) {
    Column {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            state = scrollState,
            content = {
                items(list.size) { index ->
                    WallpaperItemForVerticalStaggeredGrid(
                        wallpaper = list[index],
                        onClick = { onItemClick(list[index]) } // Invoke onItemClick lambda
                    )
                }

                scrollState.run {
                    val firstVisibleItemIndex = firstVisibleItemIndex
                    val firstItemScrollOffset = firstVisibleItemScrollOffset
                    onTopAppBarVisibilityChanged(firstVisibleItemIndex == 0 && firstItemScrollOffset == 0)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
