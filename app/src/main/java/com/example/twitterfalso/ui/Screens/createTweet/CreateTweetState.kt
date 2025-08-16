package com.example.twitterfalso.ui.Screens.createTweet

import android.net.Uri

data class CreateTweetState(
    val userId: String = "",
    val content: String = "",
    val profileImage: String? = null, //user image
    val tweetImageContent: Uri? = null, //selected image from galery
    val navigateBack: Boolean = false, // indicates if can go back

    val error: String? = null,
)
