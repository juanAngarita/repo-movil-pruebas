package com.example.twitterfalso.data.datasource

import com.example.twitterfalso.data.UserProfileInfo
import com.example.twitterfalso.data.dtos.RegisterUserDto
import com.example.twitterfalso.data.dtos.TweetDto
import com.example.twitterfalso.data.dtos.UserDtoGeneric

interface UserRemoteDataSource {

    suspend fun getUserById(id: String): UserDtoGeneric
    suspend fun getUserTweets(id: String): List<TweetDto>
    suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String): Unit
    suspend fun updateUserProfileImage(userId: String, photoUrl: String): Unit
    //
    suspend fun followOrUnfollow(currentUserId: String, targetUserId: String): Unit


}