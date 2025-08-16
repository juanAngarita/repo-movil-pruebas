package com.example.twitterfalso.data.dtos

import com.example.twitterfalso.data.TweetInfo

data class UserDto(
    val id: String,
    val username: String,
    val name: String,
    val profileImage: String? // o URL si usas `kotlinx.serialization`
) {
    constructor(): this("", "", "", "")
}

data class TweetDto(
    val userId: String,
    val id: String,
    val imageUrl: String?, // null por ahora
    val content: String,
    val likesCount: Int,
    val retweets: Int,
    val comments: Int,
    val parentTweetId: String?, // o Long?
    val createdAt: String, // en formato ISO // "2025-08-05T22:20:16.658Z"
    val updatedAt: String,
    val user: UserDto,
    //nuevo
    var liked: Boolean = false
){
    constructor(): this("", "", "", "", 0, 0, 0, null, "", "", UserDto("", "", "", ""))
}

fun TweetDto.toTweetInfo(): TweetInfo {

    return TweetInfo(
        profileImage = user.profileImage ?: "",
        name = user.name,
        username = user.username,
        content = content,
        time = createdAt,//fechaCreacion
        retweets = retweets,
        comments = comments,
        likes = likesCount,
        id = id.toString(),
        userId = userId,
        liked = liked
    )
}

