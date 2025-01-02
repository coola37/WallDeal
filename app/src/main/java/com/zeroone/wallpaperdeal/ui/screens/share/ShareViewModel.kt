package com.zeroone.wallpaperdeal.ui.screens.share

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.model.WallpapersState
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val wallpaperRepository: WallpaperRepository,
    private val userRepository: UserRepository,
    @ApplicationContext context: Context,
    private val auth: FirebaseAuth
) : ViewModel(){
    private val _state = mutableStateOf<WallpapersState>(WallpapersState())
    val state : State<WallpapersState> = _state
    val loadingState = mutableStateOf<Boolean>(false)
    val successState = mutableStateOf<Boolean?>(false)

    private var job: Job? = null

   /* private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val token = sharedPreferences.getString("firebase_token", null)*/
    private var token = ""
    init {
        auth.currentUser?.getIdToken(true)?.addOnCompleteListener { taskToken ->
            if(taskToken.isSuccessful){
                taskToken.result.token?.let {
                    token = it
                }
            }
        }
    }

    suspend fun shareWallpaper(wallpaper: Wallpaper, userId: String) {
        loadingState.value = true
        successState.value = false

        Log.e("ShareViewModelTokenControl", token)
        val user = userRepository.getUser(userId = userId, token = token)
        Log.e("UserControlShareViewModel", user.toString())
        try{
            val post = Wallpaper(
                wallpaperId= 0,
                user = user,
                description = wallpaper.description,
                category = wallpaper.category,
                likeCount = 0,
                imageUrl = wallpaper.imageUrl,
                likedUsers = Collections.emptySet(),
                userAddedFavorite = Collections.emptySet()

            )
            wallpaperRepository.saveWallpaper(wallpaper = post, token = token)
            successState.value = true
            loadingState.value = false
        }catch (e: RuntimeException){
            loadingState.value = false
            successState.value = false
            Log.e("ShareWallpaper", e.message.toString())
        }
    }
}