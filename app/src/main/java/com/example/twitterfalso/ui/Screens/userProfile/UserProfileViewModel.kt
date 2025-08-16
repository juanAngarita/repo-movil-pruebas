package com.example.twitterfalso.ui.Screens.userProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twitterfalso.data.UserProfileInfo
import com.example.twitterfalso.data.local.LocalTweetsProvider
import com.example.twitterfalso.data.repository.TweetRepository
import com.example.twitterfalso.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val tweetRepository: TweetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileState())
    val uiState: StateFlow<UserProfileState> = _uiState

    init {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val photoUrl: String = currentUser?.photoUrl?.toString() ?: ""
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        _uiState.value = _uiState.value.copy(
            user = _uiState.value.user.copy(
                profileImage = photoUrl
            ),
            currentUserId = currentUserId
        )
    }

    fun followOrUnfollow(targetUserId: String) {
        val currentUserId = _uiState.value.currentUserId
        viewModelScope.launch {
            val result = userRepository.followOrUnfollow(currentUserId, targetUserId)
            if (result.isSuccess) {
                val currentUser = _uiState.value.user
                _uiState.value = _uiState.value.copy(
                    user = currentUser.copy(followed = !currentUser.followed)
                )
            }
        }
    }

    fun deleteTweet(tweetId: String) {
        viewModelScope.launch {
            val result = tweetRepository.deleteTweet(tweetId)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    tweets = _uiState.value.tweets.filter { it.id != tweetId }
                )
            }
        }
    }

     fun getUserTweets(userId: String){
        viewModelScope.launch {
            val result = userRepository.getUserTweets(userId)
            if (result.isSuccess) {
                val userProfileInfo = result.getOrNull()!!
                _uiState.value = _uiState.value.copy(
                    tweets = userProfileInfo
                )
            }
        }
    }

     fun getUserProfile(userId: String){
        viewModelScope.launch {
            val result = userRepository.getUserById(userId)
            if (result.isSuccess) {
                val userProfileInfo = result.getOrNull()!!
                    _uiState.value = _uiState.value.copy(
                        user = userProfileInfo
                    )
            }
        }
    }


}