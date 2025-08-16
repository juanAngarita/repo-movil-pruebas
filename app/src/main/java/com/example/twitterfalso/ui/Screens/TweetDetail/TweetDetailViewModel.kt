package com.example.twitterfalso.ui.Screens.TweetDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.twitterfalso.data.TweetInfo
import com.example.twitterfalso.data.local.LocalTweetsProvider
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TweetDetailViewModel @Inject constructor(
    private val tweetRepository: TweetRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(TweetDetailState())
    val uiState: StateFlow<TweetDetailState> = _uiState

    init {
        val currentUserID = authRepository.currentUser?.uid ?: throw Exception("No user id")

        _uiState.value = _uiState.value.copy(
            currentUserId = currentUserID
        )
    }

    fun sendOrDeleteLike(tweetId: String, userId: String) {
        viewModelScope.launch {
            val result = tweetRepository.getUserTweets(userId, tweetId)
            if (result.isSuccess) {
                _uiState.update { it.copy(tweet = it.tweet?.copy(liked = !it.tweet.liked, likes = if(it.tweet.liked) it.tweet.likes - 1 else it.tweet.likes + 1)) }
            } else {
                Log.d("TAG", "sendOrDeleteLike: ${result.exceptionOrNull()}")
            }
        }
    }

    fun getTweetById(id: String) {
        val userId = authRepository.currentUser?.uid ?: throw Exception("No user id")
        viewModelScope.launch {
            val result = tweetRepository.getTweetById(id, userId)
            if (result.isSuccess) {
                val tweet = result.getOrNull()
                if (tweet != null) {
                    _uiState.update { it.copy(tweet = tweet) }
                }
            } else {
                //
            }
        }
    }

    fun getTweetRespose(id: String) {
        viewModelScope.launch {
            val result = tweetRepository.getTweetsReplies(id)
            if (result.isSuccess) {
                val tweetsReplies = result.getOrNull()
                if (tweetsReplies != null) {
                    _uiState.update { it.copy(resposeTweets = tweetsReplies) }
                }
            } else {
                //
            }
        }
    }

}