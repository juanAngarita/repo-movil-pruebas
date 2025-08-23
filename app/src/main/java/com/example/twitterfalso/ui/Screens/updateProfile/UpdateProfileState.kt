package com.example.twitterfalso.ui.Screens.updateProfile

import com.example.twitterfalso.data.UserProfileInfo

data class UpdateProfileState (
    val profileImage: String = "",
    val backgroundImage: String = "",
    val name: String = "",
    val bio: String = "",
    val location: String = "",
)