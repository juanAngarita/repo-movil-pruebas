package com.example.twitterfalso.ui.Screens.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.repository.TweetRepository
import com.example.twitterfalso.data.repository.UserRepository
import com.example.twitterfalso.ui.functions.Utils
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

        val photoUrl = Utils.getCurrentUserPhoto()
        val currentUserId = Utils.getCurrentUserId()

        _uiState.value = _uiState.value.copy(
            user = _uiState.value.user.copy(
                profileImage = photoUrl
            ),
            currentUserId = currentUserId
        )

    }

    fun followOrUnfollowUser(targetUserId: String){
        val currentUserId = _uiState.value.currentUserId ?: ""

        viewModelScope.launch {
            val result = userRepository.followOrUnfollow(currentUserId, targetUserId)
            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    user = _uiState.value.user.copy(
                        followers = if(_uiState.value.user.followed) _uiState.value.user.followers - 1 else _uiState.value.user.followers + 1,
                        followed = !_uiState.value.user.followed
                    )
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

     fun  getUserProfile(userId: String){
        viewModelScope.launch {
            val result = userRepository.getUserById(userId, Utils.getCurrentUserId())
            if (result.isSuccess) {
                val userProfileInfo = result.getOrNull()!!
                    _uiState.value = _uiState.value.copy(
                        user = userProfileInfo
                    )
            }
        }
    }

}