package com.zeroone.wallpaperdeal.ui.screens.profile

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOError
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val wallpaperRepository: WallpaperRepository,
    private val userRepository: UserRepository,
    private val wallDealRepository: WallDealRepository
) : ViewModel() {
    data class ItemsState(
        val wallpapers: List<Wallpaper> = emptyList(),
        val user: User? = null
    )

    var stateItems = mutableStateOf<ItemsState>(ItemsState())
    private var job: Job? = null

    val checkFollowState: MutableState<Boolean> = mutableStateOf(false)
    val wallDealRequestState: MutableState<Boolean> = mutableStateOf(false)
    val wallDealForTargetUserState: MutableState<Boolean> = mutableStateOf(false)
    val wallDealForBetweenUserToUserState: MutableState<Boolean> = mutableStateOf(false)
    val currentUserState: MutableState<User?> = mutableStateOf(null)

    fun fetchItems(userId: String) {
        job?.cancel()
        job = getItems(userId).onEach {
            stateItems.value = it
        }.launchIn(viewModelScope)
    }

    fun getCurrentUser(userId: String){
        try {
            viewModelScope.launch(){
                currentUserState.value =userRepository.getUser(userId = userId) }
        }catch (e: RuntimeException){
            throw e
        }
    }
    private fun getItems(userId: String): Flow<ItemsState> = flow {
        try {
            val wallpapers = wallpaperRepository.getWallpapersByOwner(userId)
            val user = userRepository.getUser(userId)

            emit(ItemsState(wallpapers = wallpapers.payload, user = user))
        } catch (e: IOError) {
            Log.e("SearchViewModel getItems error:", e.message.toString())
        }
    }

    fun sendCoupleRequest(senderUserId: String, receiverUserId: String){
        try {
            viewModelScope.launch {
                wallDealRepository.sendWallDealRequest(senderUser = senderUserId, receiverUser = receiverUserId)
                wallDealRequestState.value = true
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun checkWallDealRequest(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch {
                wallDealRequestState.value = wallDealRepository.checkWallDealRequest(currentUserId = currentUserId, targetUserId = targetUserId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun checkWallDeal(targetUserId: String){
        try {
            viewModelScope.launch {
                wallDealForTargetUserState.value = wallDealRepository.checkWalldeal(targetUserId = targetUserId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun checkWallDealForBetweenUserToUser(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch{
                wallDealForBetweenUserToUserState.value = wallDealRepository.checkWalldealForBetweenUserToUser(
                    currentUserId = currentUserId, targetUserId = targetUserId
                )
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun cancelWallDeal(userId: String){
        try {
            viewModelScope.launch{
                wallDealRepository.cancelWallDeal(userId = userId)
                wallDealForBetweenUserToUserState.value = false
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun deleteRequest(requestId: String){
        try {
            viewModelScope.launch{
                wallDealRepository.deleteRequest(requestId = requestId)
                wallDealRequestState.value = false
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    fun followOrUnFollow(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch {
                userRepository.followOrUnfollow(currentUserId = currentUserId, targetUserId = targetUserId)
                checkFollowState.value = !checkFollowState.value
                fetchItems(userId = targetUserId)
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    suspend fun checkFollow(currentUserId: String, targetUserId: String){
        try {
            viewModelScope.launch(){
                checkFollowState.value = userRepository.checkFollow(
                    currentUserId = currentUserId,
                    targetUserId = targetUserId
                )
            }
        }catch (e: RuntimeException){
            throw e
        }
    }

    suspend fun editProfile(user: User, message: () -> Unit, navigate: () -> Unit){
        var usernameUsage = false
        try {
            var users: List<User> = userRepository.getUsers()
            var currentUser = userRepository.getUser(user.userId)
            users = users.filterNot { it.userId == currentUser!!.userId }
            for(loopUser in users){
                if(loopUser.username == user.username){
                    usernameUsage = true
                    break
                }
            }
        } catch (e: CancellationException) {
            // Kullanıcı işlemi iptal etti, gerekirse burada uygun bir işlem yapılabilir
            return
        } catch (e: Exception) {
            // Diğer istisnaları burada ele alabilirsiniz
            Log.e("ProfileViewModel EditProfile Function", "An error occurred: ${e.message}")
            return
        }
        if(usernameUsage){
            message()
            Log.e("ProfileViewModel EditProfile Function", "true")
        }else{
            userRepository.editProfile(user = user)
            navigate()
            Log.e("ProfileViewModel EditProfile Function", "false")
        }
    }

    fun deleteAccount(userId: String){
        try {
            viewModelScope.launch{ userRepository.deleteAccount(userId = userId) }
        }catch (e: RuntimeException){
            throw e
        }
    }
}