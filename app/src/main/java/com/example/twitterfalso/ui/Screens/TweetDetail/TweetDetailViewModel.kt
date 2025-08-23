package com.example.twitterfalso.ui.Screens.TweetDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.TweetRepository
import com.example.twitterfalso.ui.functions.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TweetDetailViewModel @Inject constructor(
    private val tweetRepository: TweetRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(TweetDetailState())
    val uiState: StateFlow<TweetDetailState> = _uiState

    init {
        val currentUserID = Utils.getCurrentUserId()
        _uiState.value = _uiState.value.copy(
            currentUserId = currentUserID
        )
    }

    fun sendOrDeleteTweetLike(tweetId: String, userId: String) {
        viewModelScope.launch {
            val result = tweetRepository.sendOrDeleteTweetLike(tweetId, userId)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(
                        tweet = it.tweet?.copy(
                            liked = !it.tweet.liked,
                            likes = if(it.tweet.liked) it.tweet.likes - 1 else it.tweet.likes + 1)
                    )
                }
            } else {

            }
        }
    }

    fun getTweetById(id: String) {
        viewModelScope.launch {
            val result = tweetRepository.getTweetById(id, uiState.value.currentUserId)
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