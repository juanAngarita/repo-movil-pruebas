package com.example.twitterfalso.ui.Screens.TweetDetail

import com.example.twitterfalso.R
import com.example.twitterfalso.data.TweetInfo

data class TweetDetailState(
    val tweet: TweetInfo? = TweetInfo(
        profileImage = "",
        name = "Juan Pérez",
        username = "@juanp",
        content = "¡Hoy es un gran día para programar en Kotlin! 🚀",
        time = "2h",
        retweets = 15,
        comments = 8,
        likes = 120,
        id = "1",
        userId = "1"
    ),
    val resposeTweets: List<TweetInfo> = emptyList(),
    val currentUserId: String = ""
)
