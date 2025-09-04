package com.example.twitterfalso.data

data class UserProfileInfo(
    val backgroundImage: String? = null,
    val profileImage: String? = null,
    val name: String,
    val username: String,
    val bio: String,
    val followers: Int,
    val following: Int,
    val location: String,
    val website: String,
    val birthDate: String,
    val accountCreationDate: String,
    val id: String,
    val followed: Boolean = false
)
