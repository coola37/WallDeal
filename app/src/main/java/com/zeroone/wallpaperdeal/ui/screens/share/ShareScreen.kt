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

@Composable
fun ShareScreen(navController: NavController, storage: FirebaseStorage){
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var blurImage by remember { mutableStateOf<Bitmap?>(null) }
    var buttonEnabled by remember { mutableStateOf<Boolean>(false) }
    var loading by remember { mutableStateOf<Boolean>(false) }
    var wallpaperUrl by remember { mutableStateOf<String>("") }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri
            if(selectedImageUri != null){
                val bitmap = getBitmapFromUri(context, uri!!)
                blurImage = createGradiant(bitmap!!)

                val width = bitmap?.width
                val height = bitmap?.height
                if(width!! >= 1000 && height!! >= 1800){

                    buttonEnabled = true
                    Log.d("Selected photo resolution:", "${width.toString()} x ${height.toString()}" )

                }else{
                    Log.e("Selected photo resolution:", "${width.toString()} x ${height.toString()}" )
                    buttonEnabled = false
                    Toast.makeText(
                        context,
                        "The selected image does not meet the minimum resolution requirement of 1080x1920 pixels.",
                        Toast.LENGTH_SHORT
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
            TextButton(onClick = {navController.navigate(Screen.HomeScreen.route)}) {
                Text(text = "Back", color = Color.White, fontSize = 20.sp)
            }
            TextButton(onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text(text = "Pick Photo", color = Purple40, fontSize = 20.sp)
            }

            TextButton(onClick = {
                loading = true
                var wallpaperId = UUID.randomUUID().toString()

                CoroutineScope(Dispatchers.IO).launch {
                    uploadImageToFirebaseStorage(blurImage!!, selectedImageUri!!, storage, wallpaperId)

                    val bitmapRef = storage.reference.child("wallpaperGradients/${wallpaperId}")
                    val byteArray = bitmapToByteArray(blurImage!!)
                    bitmapRef.putBytes(byteArray)
                        .addOnSuccessListener {
                            Log.e("BlurrImage", "succes")
                            bitmapRef.downloadUrl.addOnSuccessListener { uri ->
                                val downloadUrl = uri.toString()
                                // Do something with the download URL if needed
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("BlurImage", e.message.toString())
                        }
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


fun createGradiant(bitmap: Bitmap) : Bitmap{
    val imageBitmap = bitmap.asImageBitmap()

    // Extract colors from the image using Palette API
    val colors = extractColorsFromBitmap(imageBitmap)

    // Create a gradient brush with the extracted colors
    val argbColors = colors.map { color ->
        android.graphics.Color.argb(
            (color.alpha * 255).toInt(),
            (color.red * 255).toInt(),
            (color.green * 255).toInt(),
            (color.blue * 255).toInt()
        )
    }.toIntArray()

    val gradientBitmap = Bitmap.createBitmap(
        imageBitmap.width,
        imageBitmap.height,
        Bitmap.Config.ARGB_8888
    )
    //gradientBitmap.asImageBitmap()
    val canvas = android.graphics.Canvas(gradientBitmap)
    canvas.drawBitmap(imageBitmap.asAndroidBitmap(), 0f, 0f, null)

    canvas.drawRect(
        0f,
        0f,
        gradientBitmap.width.toFloat(),
        gradientBitmap.height.toFloat(),
        Paint().apply {
            shader = LinearGradient(
                0f,
                0f,
                gradientBitmap.width.toFloat(),
                gradientBitmap.height.toFloat(),
                argbColors,
                null,
                Shader.TileMode.CLAMP
            )
        }
    )
    return gradientBitmap
}

fun extractColorsFromBitmap(imageBitmap: ImageBitmap): List<Color> {
    val bitmap = imageBitmap.asAndroidBitmap()
    val palette = Palette.from(bitmap).generate()
    val swatches = palette.swatches
    val colors = mutableListOf<Color>()

    for (swatch in swatches) {
        colors.add(Color(swatch.rgb))
    }

    return colors
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
fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
private suspend fun uploadImageToFirebaseStorage(bitmap: Bitmap, uri: Uri, storageRef: FirebaseStorage, storageWallpaperId: String): String = withContext(Dispatchers.IO) {
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
