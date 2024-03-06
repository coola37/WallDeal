package com.zeroone.wallpaperdeal.ui.screens.share

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.data.model.Category
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.utils.ListCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun PushWallpaperScreen(
    navController: NavController,
    storage: FirebaseStorage,
    auth: FirebaseAuth,
    viewModel: ShareViewModel = hiltViewModel()) {

    val wallpaperId = navController.currentBackStackEntry?.arguments?.getString("wallpaperId")
    var categoryText by remember { mutableStateOf<String>("") }
    var wallpaperUrl by remember { mutableStateOf<String>("") }
    Log.e("wallpaperInfo", wallpaperId!!)
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
            TextButton(onClick = { filePath.delete().addOnSuccessListener {
                Log.e("Image deleted", "succes")
                navController.navigate(Screen.HomeScreen.route)
            }.addOnFailureListener {
                Log.e("Image deleted error:", it.message.toString())
            }}) {
                Text(text = "Back", color = Color.LightGray, fontSize = 20.sp)
            }

            val wallpaper = Wallpaper(wallpaperId!!, textDexcription, null, wallpaperUrl, categoryText ,
                null, null, null )
            TextButton(onClick = {
                auth.uid?.let { userId ->
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.shareWallpaper(wallpaper, userId)
                    }}
                }, modifier = Modifier.padding(start = 240.dp)) {
                Text(text = "Share", color = Color.LightGray, fontSize = 20.sp)
            }
        }

        TextField(
            value = textDexcription,
            onValueChange = { textDexcription = it },
            colors = TextFieldDefaults.colors(Color.LightGray),
            shape = TextFieldDefaults.shape,
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
            label = { Text(text = "Write a short description") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            singleLine = false
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp, start = 12.dp)
        ) {
            Text(text = "The category you chose:", color = Color.LightGray, fontSize = 20.sp)
            Text(text = categoryText, color = Color.LightGray, fontSize = 20.sp)
        }

        Text(
            text = "Select a Category",
            fontSize = 20.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp, start = 120.dp)
        )
        fun clickItem(index: Int) {
            categoryText = categories.get(index).categoryName
        }
        Column {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    items(categories.size) { index ->
                        CategoryItem(
                            category = categories[index],
                            onClick = { clickItem(index) }
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

        }


    }
}
@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(model = category.imageUrl, contentDescription = null)
    }
}