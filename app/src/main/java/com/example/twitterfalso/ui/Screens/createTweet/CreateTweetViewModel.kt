package com.example.twitterfalso.ui.Screens.createTweet

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.repository.AuthRepository
import com.example.twitterfalso.data.repository.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTweetViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tweetRepository: TweetRepository
) : ViewModel() {



    private val _uistate = MutableStateFlow(CreateTweetState())
    val uistate: StateFlow<CreateTweetState> = _uistate

    init {
        loadUserProfileImage()
    }
    fun onTweetChange(newTweet: String) {
        _uistate.update { it.copy(content = newTweet) }
    }

    private fun loadUserProfileImage() {
        // Simulación. Usa aquí tu lógica real con coroutines si es suspend
        val photoUrl = authRepository.currentUser?.photoUrl?.toString().orEmpty()
        Log.d("TAG", "loadUserProfileImage: $photoUrl")
        _uistate.update { it.copy(profileImage = photoUrl) }
    }

    fun createTweet(parentTweetId: String? = null, tweetId: String? = null){

        val userId = authRepository.currentUser?.uid ?: return

        viewModelScope.launch {
            val result = tweetRepository.createTweet(_uistate.value.content, userId ,parentTweetId, tweetId)
            if(result.isSuccess) {
                _uistate.update { it.copy(navigateBack = true) }
            } else{
                _uistate.update { it.copy(error = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun getTweetById(Tweetid: String) {
        viewModelScope.launch {
            val result = tweetRepository.getTweetById(Tweetid)
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