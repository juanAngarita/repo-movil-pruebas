package com.example.twitterfalso.ui.Screens.createTweet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.repository.TweetRepository
import com.example.twitterfalso.ui.functions.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTweetViewModel @Inject constructor(
    private val tweetRepository: TweetRepository
) : ViewModel() {

    //State
    private val _uistate = MutableStateFlow(CreateTweetState())
    val uistate: StateFlow<CreateTweetState> = _uistate

    //Init
    init {
        loadUserProfileImage()
    }

    //Functions
    fun onTweetChange(newTweet: String) {
        _uistate.update { it.copy(content = newTweet) }
    }

    private fun loadUserProfileImage() {
        val photoUrl = Utils.getCurrentUserPhoto()
        _uistate.update { it.copy(profileImage = photoUrl) }
    }

    fun createTweet(parentTweetId: String? = null, tweetId: String? = null){

        val userId = Utils.getCurrentUserId()

        viewModelScope.launch {
            val result = tweetRepository.createTweet(_uistate.value.content, userId ,parentTweetId, tweetId)
            if(result.isSuccess) {
                _uistate.update { it.copy(navigateBack = true) }
            } else{
                _uistate.update { it.copy(error = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun getTweetById(tweetid: String) {
        viewModelScope.launch {
            val result = tweetRepository.getTweetById(tweetid)
            if (result.isSuccess) {
                val tweet = result.getOrNull()
                if (tweet != null) {
                    _uistate.update { it.copy(content = tweet.content) }
                }
            } else {
                //
            }
        }
    }



}