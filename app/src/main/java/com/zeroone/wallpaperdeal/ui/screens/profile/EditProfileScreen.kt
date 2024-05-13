package com.zeroone.wallpaperdeal.ui.screens.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.storage.FirebaseStorage
import com.zeroone.wallpaperdeal.R
import com.zeroone.wallpaperdeal.model.User
import com.zeroone.wallpaperdeal.ui.theme.ActiveButton
import com.zeroone.wallpaperdeal.ui.theme.TextFieldBaseColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    storage: FirebaseStorage
){
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false)}
    val userId: String? =  navController.currentBackStackEntry?.arguments?.getString("userId")
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri})
    userId?.let { userId ->
        var user by remember { mutableStateOf<User?>(null) }
        LaunchedEffect(key1 = userId) {
            viewModel.fetchItems(userId = userId)
        }
        var textUsername by remember { mutableStateOf("") }
        user = viewModel.stateItems.value.user
        user?.let { user ->
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.fillMaxHeight(0.02f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.04f)
                ) {
                    IconButton(onClick = { navController.navigateUp() },
                        modifier = Modifier.fillMaxWidth(0.08f)
                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                            modifier = Modifier.fillMaxSize(0.8f), tint = Color.LightGray
                        )
                    }
                    Spacer(modifier = Modifier.fillMaxWidth(0.85f))
                    IconButton(onClick = {
                        loading = true
                        val imageRef = storage.reference.child("profilePhotos/${userId}")
                        selectedImageUri?.let{
                            imageRef.putFile(it).addOnSuccessListener {
                                Log.d("Upload profile img for change", "succes")
                                imageRef.downloadUrl.addOnSuccessListener {
                                    user.userDetail?.profilePhoto = it.toString()
                                    user.username = textUsername
                                    CoroutineScope(Dispatchers.Main).launch{
                                        viewModel.editProfile(user)
                                        loading = false
                                        navController.navigateUp()
                                    }
                                }
                            }.addOnFailureListener {
                                Log.e("Upload profile img for change", "fail")
                            }
                        } ?: run {
                            user.userDetail?.profilePhoto = user.userDetail?.profilePhoto
                            user.username = textUsername
                            CoroutineScope(Dispatchers.Main).launch{
                                viewModel.editProfile(user)
                                loading = false
                                navController.navigateUp()
                            }
                        }
                    },
                        modifier = Modifier.fillMaxWidth(0.60f)) {
                        if(!loading){
                            Icon(
                                painter = painterResource(id = R.drawable.ic_ok),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(0.8f),
                                tint = Color.LightGray
                            )
                        }else{
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(30.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.15f))
                selectedImageUri?.let {
                    AsyncImage(model = it, contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight(0.25f)
                            .clip(CircleShape)
                    )
                } ?: run{
                    AsyncImage(model = user.userDetail?.profilePhoto, contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight(0.1f)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.025f))
                TextButton(onClick = { singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },){
                    Text(text = "Change Profile Photo", color = ActiveButton, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.fillMaxHeight(0.025f))
                val textFieldColor = TextFieldDefaults.colors(
                    focusedContainerColor = TextFieldBaseColor,
                    unfocusedContainerColor = TextFieldBaseColor,
                    disabledTextColor = Color.LightGray,
                    focusedTextColor = Color.LightGray,
                    unfocusedLabelColor = Color.LightGray
                )
                TextField(value = textUsername, colors = textFieldColor,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange ={ textUsername = it },
                    leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.LightGray ) },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(start = 4.dp, end = 4.dp, top = 4.dp),
                    maxLines = 1,
                    label = { Text(text = "Username", color = Color.Gray, fontSize = 14.sp) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    )
                )
            }
        }
    }
}

