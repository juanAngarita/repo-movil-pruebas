package com.example.twitterfalso.ui.Screens.SearchUser

import com.example.twitterfalso.data.UserProfileInfo

data class SearchUserState(
    val searchQuery: String = "",
    val users: List<UserProfileInfo> = emptyList(),
)
