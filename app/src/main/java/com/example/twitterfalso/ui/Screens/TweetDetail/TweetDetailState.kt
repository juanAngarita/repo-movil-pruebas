package com.example.twitterfalso.ui.Screens.TweetDetail

import com.example.twitterfalso.data.TweetInfo

data class TweetDetailState(
    val tweet: TweetInfo? = TweetInfo(),
    val resposeTweets: List<TweetInfo> = emptyList(),
    val currentUserId: String = ""
)
