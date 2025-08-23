package com.example.twitterfalso.ui.Screens.Home

import com.example.twitterfalso.data.TweetInfo

data class HomeState(
    val tweets: List<TweetInfo> = emptyList(),
    val expanded: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
