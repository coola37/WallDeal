package com.zeroone.wallpaperdeal.ui.screens.wallpaperview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.zeroone.wallpaperdeal.data.model.Report
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.ui.theme.PurpleGrey80
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ReportDialogScreen(
    onDismissRequest: () -> Unit,
    cancelDialog: () -> Unit,
    wallpaperReport: Report<Wallpaper>?,
    userReport: Report<User>?,
    reportObject: String,
    viewModel: WallpaperViewViewModel = hiltViewModel(),
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        val cardColors = CardColors(
            containerColor = PurpleGrey80,
            contentColor = PurpleGrey80,
            disabledContainerColor = PurpleGrey80,
            disabledContentColor = PurpleGrey80
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = cardColors
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Text(
                    text = "Why are you reporting this ${reportObject}?",
                    modifier = Modifier.padding(16.dp), color = Color.DarkGray
                )
                val textFieldColor = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledTextColor = Color.LightGray,
                    focusedTextColor = Color.Black,
                    unfocusedLabelColor = Color.LightGray
                )
                var textReport by remember{ mutableStateOf("") }
                TextField(value = textReport, colors = textFieldColor,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange ={ textReport = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Report, contentDescription = null, tint = Color.Red ) },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                    maxLines = 3,
                    label = { Text(text = "Desc.", color = Color.Gray, fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss", color = Color.DarkGray)
                    }
                    TextButton(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                when(reportObject){
                                    "post" -> {
                                        wallpaperReport?.let{
                                            it.message = textReport
                                            viewModel.reportWallpaper(report = it)
                                        }
                                    }
                                    "user" -> {
                                        userReport?.let {
                                            it.message = textReport
                                            viewModel.reportUser( report = it)
                                        }

                                    }
                                }

                            }
                            cancelDialog()
                        },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Send", color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun previewDialog(){
    //ReportDialogScreen()
}