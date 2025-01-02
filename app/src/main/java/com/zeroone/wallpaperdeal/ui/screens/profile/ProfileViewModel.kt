package com.zeroone.wallpaperdeal.ui.screens.profile

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.zeroone.wallpaperdeal.data.model.User
import com.zeroone.wallpaperdeal.data.model.Wallpaper
import com.zeroone.wallpaperdeal.data.remote.repository.UserRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallDealRepository
import com.zeroone.wallpaperdeal.data.remote.repository.WallpaperRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOError
import java.util.Collections
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val wallpaperRepository: WallpaperRepository,
    private val userRepository: UserRepository,
    private val wallDealRepository: WallDealRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    data class ItemsState(
        val wallpapers: Set<Wallpaper> = Collections.emptySet(),
        val user: User? = null
    )

    var stateItems = mutableStateOf<ItemsState>(ItemsState())
    private var job: Job? = null

    val checkFollowState: MutableState<Boolean> = mutableStateOf(false)
    val wallDealRequestState: MutableState<Boolean> = mutableStateOf(false)
    val wallDealForTargetUserState: MutableState<Boolean> = mutableStateOf(false)
    val wallDealForBetweenUserToUserState: MutableState<Boolean> = mutableStateOf(false)
    val currentUserState: MutableState<User?> = mutableStateOf(null)
    val favoriteWallpapers: MutableState<List<Wallpaper>> = mutableStateOf(emptyList())

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    private val token = sharedPreferences.getString("firebase_token", null)

    fun fetchItems(userId: String) {
        job?.cancel()
        job = getItems(userId).onEach {
            stateItems.value = it
        }.launchIn(viewModelScope)
    }

    fun getCurrentUser(userId: String){
        try {
            viewModelScope.launch(){
                token?.let {
                    currentUserState.value =userRepository.getUser(userId = userId, token = it) }
                }
        }catch (e: RuntimeException){
            throw e
        }
    }
    private fun getItems(userId: String): Flow<ItemsState> = flow {
        try {
            token?.let {
                val wallpapers = wallpaperRepository.getWallpapersByOwner(ownerId = userId, token = it)
                val user = userRepository.getUser(userId = userId, token = it)
                emit(ItemsState(wallpapers = wallpapers.payload.toSet(), user = user))
            }
        } catch (e: IOError) {
            Log.e("SearchViewModel getItems error:", e.message.toString())
        }
    }

    fun sendCoupleRequest(senderUserId: String, receiverUserId: String){
        try {
            viewModelScope.launch {
               token?.let {
                   wallDealRepository.sendWallDealRequest(senderUser = senderUserId, receiverUser = receiverUserId, token = it)
                   wallDealRequestState.value = true
               }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun checkWallDealRequest(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch {
                token?.let {
                    wallDealRequestState.value = wallDealRepository.checkWallDealRequest(
                        currentUserId = currentUserId,
                        targetUserId = targetUserId,
                        token = it
                    )

                }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    suspend fun getFavoriteWallpaper(userId: String){
        token?.let{
            favoriteWallpapers.value = wallpaperRepository.getFavorites(userId = userId, token = it)
        }
    }

    fun checkWallDeal(targetUserId: String){
        try {
            viewModelScope.launch {
                token?.let {
                }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun checkWallDealForBetweenUserToUser(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch{
                token?.let {
                    wallDealForBetweenUserToUserState.value = wallDealRepository.checkWalldealForBetweenUserToUser(
                        currentUserId = currentUserId, targetUserId = targetUserId, token = it
                    )
                }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun cancelWallDeal(userId: String){
        try {
            viewModelScope.launch{
                token?.let {
                    wallDealRepository.cancelWallDeal(userId = userId, token = it)
                    wallDealForBetweenUserToUserState.value = false
                }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun deleteRequest(requestId: String){
        try {
            viewModelScope.launch{
                token?.let {
                    wallDealRepository.deleteRequest(requestId = requestId, token = it)
                    wallDealRequestState.value = false
                }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun followOrUnFollow(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch {
                token?.let {
                    userRepository.followOrUnfollow(currentUserId = currentUserId, targetUserId = targetUserId, token = it)
                    checkFollowState.value = !checkFollowState.value
                    fetchItems(userId = targetUserId)
                }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    suspend fun checkFollow(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch(){
                token?.let {
                    checkFollowState.value = userRepository.checkFollow(
                        currentUserId = currentUserId,
                        targetUserId = targetUserId,
                        token = it
                    )
                }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    suspend fun editProfile(user: User, message: () -> Unit, navigate: () -> Unit){
        var usernameUsage = false
        token?.let {
            try {
                val token = sharedPreferences.getString("firebase_token", null)
                Log.e("Token: ", "Bearer $token")
                var users: List<User> = userRepository.getUsers(token= it)
                var currentUser = userRepository.getUser(userId = user.userId, token = it)
                users = users.filterNot { it.userId == currentUser!!.userId }
                for(loopUser in users){
                    if(loopUser.username == user.username){
                        usernameUsage = true
                        break
                    }
                }
            } catch (e: CancellationException) {
                return
            } catch (e: Exception) {
                Log.e("ProfileViewModel EditProfile Function", "An error occurred: ${e.message}")
                return
            }
            if(usernameUsage){
                message()
                Log.e("ProfileViewModel EditProfile Function", "true")
            }else{
                userRepository.editProfile(user = user, token = it)
                navigate()
                Log.e("ProfileViewModel EditProfile Function", "false")
            }
        }
    }

    fun deleteAccount(userId: String){
        try {
            token?.let {
                viewModelScope.launch{ userRepository.deleteAccount(userId = userId, token = it) }
            }
        }catch (e: RuntimeException){
            throw e
        }
    }
}