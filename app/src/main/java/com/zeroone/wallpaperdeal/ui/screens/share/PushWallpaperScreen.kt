package com.zeroone.wallpaperdeal.ui.screens.share

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.data.model.Category
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.TextFieldBaseColor
import com.zeroone.wallpaperdeal.utils.ListCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections


@SuppressLint("SuspiciousIndentation")
@Composable
fun PushWallpaperScreen(
    navController: NavController,
    storage: FirebaseStorage,
    auth: FirebaseAuth,
    viewModel: ShareViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val wallpaperId = navController.currentBackStackEntry?.arguments?.getString("wallpaperId")
    wallpaperId?.let {
        var categoryText by remember { mutableStateOf<String>("") }
        var wallpaperUrl by remember { mutableStateOf<String>("") }
        var gradientUrl by remember { mutableStateOf<String>("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            var textDexcription: String by remember { mutableStateOf("") }
            val categories = ListCategory.list
            val filePath = storage.reference.child("wallpapers/${wallpaperId}")
            filePath.downloadUrl.addOnSuccessListener {
                wallpaperUrl = it.toString()
            }.addOnFailureListener {
                Log.e("DownloadUrlError", it.toString())
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Spacer(modifier = Modifier.fillMaxWidth(0.05f))
                IconButton(onClick = { filePath.delete().addOnSuccessListener {
                    Log.e("Image deleted", "succes")
                    navController.navigate(
                        Screen.HomeScreen.route,
                        navOptions { popUpTo(Screen.HomeScreen.route) { inclusive = true } }
                    )
                }.addOnFailureListener {
                    Log.e("Image deleted error:", it.message.toString())
                }}, enabled = !viewModel.loadingState.value, modifier = Modifier.fillMaxWidth(0.1f)) {
                    Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null)
                }
                Spacer(modifier = Modifier.fillMaxWidth(0.75f))
                val wallpaper = Wallpaper(
                    wallpaperId= 0,
                    user = null,
                    description = textDexcription,
                    category = categoryText,
                    likeCount = 0,
                    imageUrl = wallpaperUrl,
                    likedUsers = Collections.emptySet(),
                    userAddedFavorite = Collections.emptySet()
                )
                TextButton(onClick = {
                    auth.uid?.let { userId ->
                        CoroutineScope(Dispatchers.Main).launch {
                            viewModel.shareWallpaper(wallpaper, userId)
                            if(categoryText != ""){
                                if(viewModel.successState.value!!){
                                    navController.navigate(
                                        Screen.HomeScreen.route,
                                        navOptions { popUpTo(Screen.HomeScreen.route) { inclusive = true } }
                                    )
                                }else{
                                    Toast.makeText(context, "Wallpaper has not sent", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(context, "Please select category", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }, modifier = Modifier.fillMaxWidth(1f)) {
                    if(!viewModel.loadingState.value){
                        Text(text = "Share", color = Color.LightGray, fontSize = 16.sp)
                    }else{
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            }
            TextField(
                value = textDexcription,
                onValueChange = { textDexcription = it },
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color.LightGray,
                    unfocusedContainerColor = TextFieldBaseColor,
                    focusedContainerColor = TextFieldBaseColor,
                    focusedTextColor = Color.LightGray,
                    unfocusedLabelColor = Color.LightGray,
                    unfocusedTextColor = Color.LightGray

                ),
                shape = CircleShape,
                modifier = Modifier
                    .background(color = Color.Unspecified)
                    .fillMaxWidth()
                    .focusRequester(FocusRequester())
                    .padding(horizontal = 16.dp),  // Adjust padding as needed
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ImageSearch,
                        contentDescription = null
                    )
                },
                label = { Text(text = "Write a short description", fontSize = 14.sp) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                maxLines = 2,
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
                Text(
                    text = "Select a Category",
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold,
                )
            }

            var selectedCategoryIndex by remember { mutableStateOf(-1) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = 4.dp,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    content = {
                        items(categories.size) { index ->
                            CategoryItem(
                                category = categories[index],
                                selected = index == selectedCategoryIndex,
                                onSelectedChange = { isSelected ->
                                    if (isSelected) {
                                        categoryText = categories[index].categoryName
                                        selectedCategoryIndex = index
                                    } else {
                                        selectedCategoryIndex = -1
                                    }
                                }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
@Composable
fun CategoryItem(category: Category, selected: Boolean, onSelectedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onSelectedChange(!selected)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f)
                .background(if (selected) Color.LightGray else Color.Transparent)
        ) {
            AsyncImage(model = category.imageUrl, contentDescription = null)
        }
    }
}