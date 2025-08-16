package com.example.twitterfalso.ui.Screens.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twitterfalso.data.local.LocalTweetsProvider
import com.example.twitterfalso.data.repository.TweetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val tweetRepository: TweetRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    fun onExpandedChange(){
        val valorActual = _uiState.value.expanded
        _uiState.update { it.copy(expanded = !valorActual) }
    }

    fun getAllTweets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            tweetRepository.getTweetsLive()
                .catch { e ->
                    _uiState.update { it.copy(errorMessage = e.message, isLoading = false) }
                }
                .collect { tweets ->
                    _uiState.update { it.copy(tweets = tweets, isLoading = false, errorMessage = null) }
                }
        }
    }

    init {
        //getAllTweets()
    }

}