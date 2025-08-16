package com.example.twitterfalso.data.dtos

data class CreateTweetUserDto(
    val name: String? = null,
    val username: String? = null,
    val profileImage: String? = null
)

data class CreateTweetDto(
    val content: String,
    var userId: String,
    val parentTweetId: String?,
    val tweeetId: String? = null,

    var user: CreateTweetUserDto? = null
)
