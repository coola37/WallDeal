package com.zeroone.wallpaperdeal.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.TextFieldBaseColor
import kotlinx.coroutines.Dispatchers


@Composable
fun SearchScreen(
    navController: NavController,
    auth: FirebaseAuth,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var searchText by remember { mutableStateOf<String>("") }
    var searchMethod by remember { mutableStateOf("wallpapers") }
    var wallpapers by remember { mutableStateOf<List<Wallpaper>>(emptyList()) }
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    LaunchedEffect(Dispatchers.IO){
        viewModel.getLocalItems()
    }
    wallpapers = viewModel.stateItems.value.wallpapers
    users = viewModel.stateItems.value.users

    Scaffold(
        backgroundColor = Color.Black,
        bottomBar = { BottomNavigationBar(selectedItem = 1, navController = navController) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            val textFieldColor = TextFieldDefaults.colors(
                focusedContainerColor = TextFieldBaseColor,
                unfocusedContainerColor = TextFieldBaseColor,
                disabledTextColor = Color.LightGray,
                focusedTextColor = Color.LightGray,
                unfocusedLabelColor = Color.LightGray
            )
            TextField(value = searchText, colors = textFieldColor,
                shape = RoundedCornerShape(20.dp),
                onValueChange ={
                    searchText = it
                    when (searchMethod) {
                        "wallpapers" -> {
                            wallpapers = filterList(it, viewModel.stateItems.value.wallpapers) { wallpaper ->
                                wallpaper.description!!.contains(it, ignoreCase = true)
                            }
                        }
                        "accounts" -> {
                            users = filterList(it, viewModel.stateItems.value.users) { user ->
                                user.username.contains(it, ignoreCase = true)
                            }
                        }
                    }
                },
                leadingIcon = { Icon(imageVector = Icons.Default.ImageSearch, contentDescription = null, tint = Color.LightGray )},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                maxLines = 1,
                label = { Text(text = if (searchMethod == "wallpapers")  "Search by Description" else "Search by Username",
                    color = Color.Gray, fontSize = 14.sp) }
            )
            Row(modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .background(Color.Black)) {
                TextButton(onClick = { searchMethod = "wallpapers" }, modifier = Modifier.padding(start = 65.dp)) {
                    when(searchMethod){
                        "wallpapers" ->{Text(text = "Wallpapers", color = Color.White, fontSize = 14.sp)}
                        "accounts" -> {Text(text = "Wallpapers", color = Color.Gray, fontSize = 14.sp)}
                    }
                }
                TextButton(onClick = {searchMethod = "accounts"}, modifier = Modifier.padding(start = 50.dp)) {
                    when(searchMethod){
                        "wallpapers" ->{Text(text = "Accounts", color = Color.Gray, fontSize = 14.sp)}
                        "accounts" -> {Text(text = "Accounts", color = Color.White, fontSize = 14.sp)}
                    }
                }
            }

            if(wallpapers.isNotEmpty() || users.isNotEmpty()){
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(if (searchMethod == "wallpapers") wallpapers else users) { item ->
                        when (searchMethod) {
                            "wallpapers" -> WallpaperItemForSearch(wallpaper = item as Wallpaper, navController = navController)
                            "accounts" -> UserItemForSearch(user = item as User, navController)
                        }
                    }
                }
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

@Composable
private fun WallpaperItemForSearch(wallpaper: Wallpaper, navController: NavController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { navController.navigate("${Screen.WallpaperViewScreen.route}/${wallpaper.wallpaperId}") }) {
        wallpaper.let {
            AsyncImage(model = wallpaper.imageUrl, contentDescription = null, modifier = Modifier
                .width(70.dp)
                .height(100.dp)
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.03f))
            Column(modifier = Modifier.height(100.dp))
            {
                Spacer(modifier = Modifier.fillMaxHeight(0.15f))
                Text(text = wallpaper.user!!.username, color = Color.LightGray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = wallpaper.category, color = Color.Gray, fontSize = 14.sp )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun UserItemForSearch(user: User, navController: NavController){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp, top = 8.dp)
        .clickable { navController.navigate("${Screen.OtherProfileScreen.route}/${user.userId}") }){
         AsyncImage(model = user.profilePhoto, contentDescription = null, modifier = Modifier
             .width(80.dp)
             .height(60.dp)
             .padding(start = 8.dp)
             .clip(CircleShape)
         )
        Text(text = user.username, color = Color.White, fontSize = 14.sp,modifier = Modifier
            .padding(top = 16.dp, start = 16.dp)
        )
    }
}
fun <T> filterList(input: String, list: List<T>, filterFunction: (T) -> Boolean): List<T> {
    return list.filter(filterFunction)
}