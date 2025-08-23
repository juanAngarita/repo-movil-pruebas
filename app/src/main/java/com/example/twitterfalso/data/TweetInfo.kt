package com.example.twitterfalso.data

import androidx.annotation.DrawableRes

/*Este Tweet info se vuelve posteriormente el objeto de UI*/
/*Es necesario crear un DTO para separar responsabilidades*/
data class TweetInfo(
    val profileImage: String,
    val id: String,
    val name: String,
    val username: String,
    val content: String,
    val time: String, //"2h""1D"
    val retweets: Int,
    val likes: Int,
    val comments: Int,
    val userId: String,
    //
    val liked: Boolean = false,
){
    constructor() : this ("","","","","","",0,0,0,"",false)
}