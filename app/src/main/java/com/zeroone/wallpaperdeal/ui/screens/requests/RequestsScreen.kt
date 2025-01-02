package com.zeroone.wallpaperdeal.ui.screens.requests

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.platform.LocalContext
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
import com.zeroone.wallpaperdeal.data.model.Couple
import com.zeroone.wallpaperdeal.data.model.CoupleRequest
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.DeleteColor
import com.zeroone.wallpaperdeal.ui.theme.ProfileButtonColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RequestsScreen(
    auth: FirebaseAuth,
    navController: NavController,
    viewModel: RequestsViewModel = hiltViewModel()
){
    var requests by remember { mutableStateOf<List<CoupleRequest>?>(null) }
    val context = LocalContext.current

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
           IconButton(onClick = { navController.navigateUp() }) {
               Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null, tint = Color.LightGray)
           }
           Spacer(modifier = Modifier.fillMaxWidth(0.15f))
           Text(text = "Notifications", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(top = 6.dp))
       }
        Spacer(modifier = Modifier.fillMaxWidth(0.1f))
        requests?.let { requests ->
            LazyColumn(modifier = Modifier.fillMaxSize()){
                items(requests.size){
                    RequestItem(request = requests[it],
                        createWalldeal = {
                            CoroutineScope(Dispatchers.Main).launch{
                               try {
                                   val request = requests[it]
                                   if(request.senderUser.coupleId.isNullOrEmpty()){
                                       Log.e("Request Screen Request Control", request.toString())
                                       request?.let {
                                           val couple = Couple(
                                               groupId = request.coupleRequestId,
                                               user1 = request.senderUser,
                                               user2 = request.receiverUser,
                                               requestId = ""
                                           )
                                           viewModel.createWallDeal(couple = couple)
                                       }

                                   }else{
                                       Toast.makeText(
                                           context,
                                           "The request was deleted because the user already had a couple.",Toast.LENGTH_LONG)
                                           .show()
                                       viewModel.deleteWallDealRequest(requestId = request.coupleRequestId)
                                   }
                               }finally {
                                   navController.navigate(Screen.WallDealScreen.route)
                               }
                            }
                        },
                        deleteRequest = {
                            try {
                                viewModel.deleteWallDealRequest(requests[it].coupleRequestId)
                            }finally {
                                navController.navigate(Screen.HomeScreen.route)
                            }
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun RequestItem(request: CoupleRequest, createWalldeal: () -> Unit, deleteRequest: () -> Unit ){
    Column(){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
        ) {
            Spacer(modifier = Modifier.fillMaxWidth(0.02f))
            AsyncImage(
                model = request.senderUser.profilePhoto, contentDescription = null,
                Modifier
                    .clip(CircleShape)
                    .fillMaxWidth(0.1f)
                    .fillMaxHeight(0.05f)
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.05f))
            Text(
                text = request.title,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.fillMaxHeight(0.25f))
        Row(modifier = Modifier.fillMaxWidth()){
            Spacer(modifier = Modifier.fillMaxWidth(0.30f))
            Button(
                colors = ButtonDefaults.buttonColors(ProfileButtonColor),
                onClick = { createWalldeal()},
                modifier = Modifier
            ) {
                Text(text = "Accept", color = Color.LightGray, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
            Button(
                colors = ButtonDefaults.buttonColors(DeleteColor),
                onClick = {deleteRequest()},
                modifier = Modifier
            ) {
                Text(text = "Delete", color = Color.LightGray, fontSize = 16.sp)
            }
            }
        }

}
@Preview
@Composable
fun PreviewScreen(){
    //RequestItem()
}