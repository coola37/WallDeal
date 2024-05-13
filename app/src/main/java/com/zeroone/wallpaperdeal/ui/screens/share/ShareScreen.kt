package com.zeroone.wallpaperdeal.ui.screens.share

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.ui.screens.Screen
import com.zeroone.wallpaperdeal.ui.theme.Purple40
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.util.UUID
import kotlin.coroutines.resumeWithException
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.zeroone.wallpaperdeal.utils.Constant.HEIGHT_PX
import com.zeroone.wallpaperdeal.utils.Constant.WEIGHT_PX

@Composable
fun ShareScreen(navController: NavController, storage: FirebaseStorage){
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var buttonEnabled by remember { mutableStateOf<Boolean>(false) }
    var loading by remember { mutableStateOf<Boolean>(false) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri
            if(selectedImageUri != null){
                val bitmap = getBitmapFromUri(context, uri!!)
                val width = bitmap?.width
                val height = bitmap?.height
                if(width!! >= WEIGHT_PX && height!! >= HEIGHT_PX){

                    buttonEnabled = true
                    Log.d("Selected photo resolution:", "${width.toString()} x ${height.toString()}" )

                }else{
                    Log.e("Selected photo resolution:", "${width.toString()} x ${height.toString()}" )
                    buttonEnabled = false
                    Toast.makeText(
                        context,
                        "The resolution of the image you want to upload must be QHD",
                        Toast.LENGTH_LONG
                    ).show()
                    selectedImageUri = null
                }
            }else{
                buttonEnabled = false
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(Modifier.weight(1f)) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(onClick = {navController.navigate(Screen.HomeScreen.route)}, enabled = !loading) {
                Text(text = "Back", color = Color.White, fontSize = 20.sp)
            }
            TextButton(onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }, enabled = !loading) {
                Text(text = "Pick Photo", color = Purple40, fontSize = 20.sp)
            }

            TextButton(onClick = {
                loading = true
                var wallpaperId = UUID.randomUUID().toString()
                CoroutineScope(Dispatchers.Main).launch {
                    uploadImageToFirebaseStorage(selectedImageUri!!, storage, wallpaperId)
                    navController.navigate("${Screen.PushWallpaperScreen.route}/${wallpaperId}")
                }
                                 },
                enabled = buttonEnabled) {
                if(!buttonEnabled){
                    Text(text = "Next", color = Color.Gray, fontSize = 20.sp)
                }else{
                    if(!loading){
                        Text(text = "Next", color = Color.White, fontSize = 20.sp)
                    }else{
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
            }
        }
    }
}
fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        bitmap = BitmapFactory.decodeStream(inputStream)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    return bitmap
}
private suspend fun uploadImageToFirebaseStorage(uri: Uri, storageRef: FirebaseStorage, storageWallpaperId: String): String = withContext(Dispatchers.IO) {
    val imageRef = storageRef.reference.child("wallpapers/${storageWallpaperId}")

    return@withContext suspendCancellableCoroutine { continuation ->
        val uploadTask = imageRef.putFile(uri)

        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                continuation.resume(downloadUrl.toString()) {
                    uploadTask.cancel()
                }
            }.addOnFailureListener { urlException ->
                continuation.resumeWithException(urlException)
            }
        }.addOnFailureListener { uploadException ->
            continuation.resumeWithException(uploadException)
        }

        continuation.invokeOnCancellation {
            uploadTask.cancel()
        }
    }
}
