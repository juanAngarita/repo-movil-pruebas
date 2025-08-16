package com.example.twitterfalso.ui.Screens.userProfile

import com.example.twitterfalso.data.TweetInfo
import com.example.twitterfalso.data.UserProfileInfo

data class UserProfileState(
    val tweets: List<TweetInfo> = emptyList(),
    val user: UserProfileInfo = UserProfileInfo("", "", "", "", "", 0, 0, "", "", "", "", ""),
    val userId: String? = null,
    val currentUserId: String = "",
)
