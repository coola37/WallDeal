package com.zeroone.wallpaperdeal.ui.screens.search

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroone.wallpaperdeal.ui.BottomNavigationBar
import com.zeroone.wallpaperdeal.ui.theme.TextFieldBaseColor
import com.zeroone.wallpaperdeal.ui.theme.ThemeGray

@Composable
fun WallpaperSearchScreen(){
    var searchText by remember { mutableStateOf<String>("") }
    var searchMethod by remember { mutableStateOf("wallpapers") }
    val topTextColor = Color.White
    Scaffold(
        backgroundColor= Color.Black,
        bottomBar = { BottomNavigationBar(selectedItem = 1, navController = null) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
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
                },
                leadingIcon = { Icon(imageVector = Icons.Default.ImageSearch, contentDescription = null, tint = Color.LightGray )},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                maxLines = 1,
                label = { Text(text = "Search", color = Color.Gray, fontSize = 18.sp) }
            )
            TopSelectedText(
                onClickAccounts = { searchMethod = "accounts" },
                onclickWallpapers = {searchMethod = "wallpapers"},
                selected = searchMethod)
            /*if(searchMethod.equals("wallpapers")){
                Canvas(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 65.dp)) {
                    val start = Offset(0f, size.height / 2)
                    val end = Offset(300f, size.height / 2)
                    drawLine(
                        start = start,
                        end = end,
                        color = ThemeGray,
                        strokeWidth = 5f,
                        cap = StrokeCap.Round
                    )
                }
            }*/
        }

    }
}

@Composable
private fun TopSelectedText(onClickAccounts: () -> Unit , onclickWallpapers: () -> Unit, selected: String){
    Row(modifier = Modifier
        .padding(top = 0.dp)
        .fillMaxWidth()
        .background(Color.Black)) {
        TextButton(onClick = { onclickWallpapers }, modifier = Modifier.padding(start = 65.dp)) {
            if (selected == "wallpapers"){
                Text(text = "Wallpapers", color = Color.White, fontSize = 16.sp)
            }else{
                Text(text = "Wallpapers", color = Color.Gray, fontSize = 16.sp)
            }
        }
        TextButton(onClick = {onClickAccounts}, modifier = Modifier.padding(start = 50.dp)) {
            if(selected == "accounts"){
                Text(text = "Accounts", color = Color.White, fontSize = 16.sp)
            }else{
                Text(text = "Accounts", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}

@Preview
@Composable
fun PreviewWallpaperSearch(){
    WallpaperSearchScreen()
}