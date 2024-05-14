package com.zeroone.wallpaperdeal.ui

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.screens.Screen

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
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 6.dp)
            .clickable(onClick = onClick),
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

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    text: String = "Sign Up with Google",
    loadingText: String = "Creating Account...",
    icon: Int = R.drawable.ic_google_logo,
    shape: Shape = MaterialTheme.shapes.medium,
    borderColor: Color = Color.LightGray,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    onClicked: () -> Unit,
) {
    var clicked by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                clicked = !clicked
                onClicked()
            },
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 12.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = icon),
                contentDescription = "Google Button",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            androidx.compose.material3.Text(text = if (clicked) loadingText else text)
            if (clicked) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}

@Composable
fun ButtonLoginAndRegister(
    modifier: Modifier = Modifier,
    text: String,
    loadingText: String,
    onClicked: () -> Unit,
    enabled: Boolean,
    enabledText: String
) {
    val context = LocalContext.current
    var clicked by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable {
                clicked = !clicked
                if(enabled){
                    onClicked()
                }else{
                    Toast.makeText(context, enabledText, Toast.LENGTH_LONG).show()
                }
            },
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .padding(
                    start = 18.dp,
                    end = 24.dp,
                    top = 18.dp,
                    bottom = 18.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = if (clicked && enabled) loadingText else text, color = Color.White, fontSize = 12.sp)
            if (clicked && enabled) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
