package com.zeroone.wallpaperdeal.ui.screens.Notifications

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zeroone.wallpaperdeal.R

@Composable
fun NotificationScreen(){

    Scaffold(containerColor = Color.Black) {
        Column(modifier = Modifier.padding(it)) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()){
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(24.dp))
                Text(text = "Notifications", color = Color.White, fontSize = 24.sp)
            }

            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(content = {})
            }
        }
    }
}

@Composable
fun NotificationItem(){
 /*  Column(modifier = Modifier.fillMaxWidth()) {
       Row {
           Text(text = "Coola37 sent Walldeal request", fontSize = 20.sp, color = Color.LightGray)
           Spacer(modifier = Modifier.padding(8.dp))
           Image(painter = painterResource(id = R.drawable.walldeal_ok), contentDescription = null,
               modifier = Modifier
                   .clip(CircleShape)
                   .size(35.dp))
           Spacer(modifier = Modifier.padding(16.dp))
           Image(painter = painterResource(id = R.drawable.cancel), contentDescription = null,
               modifier = Modifier
                   .clip(CircleShape)
                   .size(35.dp))
       }
   }*/
    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            AsyncImage(model = "", contentDescription = null, modifier = Modifier.clip(CircleShape).size(40.dp))
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = "Coola37 sent Walldeal request", fontSize = 20.sp, color = Color.LightGray)
        }
    }
}
@Preview
@Composable
fun PreviewRequestScreen(){
    NotificationItem()
}