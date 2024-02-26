package com.zeroone.wallpaperdeal.ui.screens.ui

import android.util.Log
import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.BottomAppbarColor
import com.zeroone.wallpaperdeal.ui.theme.Purple40
import com.zeroone.wallpaperdeal.ui.theme.ThemeGray
import com.zeroone.wallpaperdeal.ui.theme.TopAppBarColor

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
fun BottomNavigationBar(selectedItem: Int, navController: NavController) {
    var selectedItem by remember { mutableStateOf(selectedItem) }

    BottomAppBar(backgroundColor = Color.Black) {
        BottomNavigation {
            val items = listOf(
                BottomNavItem("Home", R.drawable.ic_home, 0),
                BottomNavItem("Search", R.drawable.ic_search, 1),
                BottomNavItem("Profile", R.drawable.ic_share, 2),
                BottomNavItem("Settings", R.drawable.ic_group, 3),
                BottomNavItem("Settings", R.drawable.ic_profile, 4)
            )

            items.forEach { item ->
                BottomNavigationItem(
                    selected = selectedItem == item.index,
                    onClick = { navController.navigate(Screen.ShareScreen.route)},
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

data class BottomNavItem(val label: String, val icon: Int, val index: Int)