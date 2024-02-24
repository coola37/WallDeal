package com.zeroone.wallpaperdeal.ui.screens.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.ui.theme.AppbarGray
import com.zeroone.wallpaperdeal.ui.theme.BottomAppbarColor

@Composable
fun TopAppbarText(){
    Row(modifier = Modifier
        .padding(top = 0.dp)
        .fillMaxWidth()
        .background(AppbarGray)) {
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "Home", color = Color.LightGray, fontSize = 16.sp,
                modifier = Modifier.padding(start = 85.dp))
        }
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "Categories", color = Color.LightGray, fontSize = 16.sp, modifier = Modifier.padding(start = 50.dp))
        }
    }
}

@Composable
fun BottomNavigationBar(){
    androidx.compose.material.BottomAppBar(
        backgroundColor = AppbarGray,
        contentColor = Color.White,
        cutoutShape = CircleShape
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp)
        ) {
            IconButton(onClick = { /* TODO: Handle Home button click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Home"
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            IconButton(onClick = { /* TODO: Handle Categories button click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Categories"
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            IconButton(onClick = { /* TODO: Handle Categories button click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = "Categories"
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            IconButton(onClick = { /* TODO: Handle Categories button click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_group),
                    contentDescription = "Categories"
                )
            }
            Spacer(modifier = Modifier.width(24.dp))
            IconButton(onClick = { /* TODO: Handle Categories button click */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Categories"
                )
            }
        }
    }
}