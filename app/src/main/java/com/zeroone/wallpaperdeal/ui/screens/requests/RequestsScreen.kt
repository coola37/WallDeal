package com.zeroone.wallpaperdeal.ui.screens.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.model.WallDealRequest
import com.zeroone.wallpaperdeal.ui.screens.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RequestsScreen(
    auth: FirebaseAuth,
    navController: NavController,
    viewModel: RequestsViewModel = hiltViewModel()
){
    var requests by remember { mutableStateOf<List<WallDealRequest>?>(null) }

    LaunchedEffect(key1 = auth.uid){
        viewModel.fetchRequest(userId = auth.uid!!)
    }
    requests = viewModel.requestsState.value
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
       Row(modifier = Modifier
           .fillMaxWidth()
           .fillMaxSize(0.05f)) {
           IconButton(onClick = { /*TODO*/ }) {
               Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null, tint = Color.LightGray)
           }
           Spacer(modifier = Modifier.fillMaxWidth(0.1f))
           Text(text = "Notifications", color = Color.LightGray, fontSize = 20.sp, modifier = Modifier.padding(top = 6.dp))
       }
        requests?.let { requests ->
            LazyColumn(modifier = Modifier.fillMaxSize()){
                items(requests.size){
                    RequestItem(request = requests[it],
                        createWalldeal = {
                            CoroutineScope(Dispatchers.Main).launch{
                                viewModel.createWallDeal(requests[it].possibleWallDeal)
                                delay(1000)
                                navController.navigate(Screen.WallDealScreen.route)
                            }
                        },
                        deleteRequest = {
                            viewModel.deleteWallDealRequest(requests[it].wallDealRequestId)
                            navController.navigate(Screen.HomeScreen.route)
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun RequestItem(request: WallDealRequest, createWalldeal: () -> Unit, deleteRequest: () -> Unit ){
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.Black)) {
        AsyncImage(model = request.senderUser.userDetail?.profilePhoto, contentDescription = null,
            Modifier
                .clip(CircleShape)
                .fillMaxWidth(0.1f)
                .height(40.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = request.title, color = Color.LightGray,textAlign = TextAlign.Center, modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth(.8f))
        IconButton(onClick = { createWalldeal() },modifier = Modifier.fillMaxWidth(0.4f)) {
            Icon(painter = painterResource(id = R.drawable.walldeal_ok), contentDescription = null, tint = Color.Unspecified,
                modifier = Modifier.fillMaxSize())
        }
        Spacer(modifier = Modifier.fillMaxWidth(0.2f))
        IconButton(onClick = { deleteRequest() },modifier = Modifier.fillMaxWidth(0.8f)) {
            Icon(painter = painterResource(id = R.drawable.cancel), contentDescription = null, tint = Color.Unspecified,
                modifier = Modifier.fillMaxSize())
        }
    }
}
@Preview
@Composable
fun PreviewScreen(){
    //RequestItem()
}