package com.example.twitterfalso.data.datasource.impl.retrofit

import com.example.twitterfalso.data.datasource.UserRemoteDataSource
import com.example.twitterfalso.data.datasource.services.UserRetrofitService
import com.example.twitterfalso.data.dtos.RegisterUserDto
import com.example.twitterfalso.data.dtos.TweetDto
import com.example.twitterfalso.data.dtos.UpdateUserDto
import com.example.twitterfalso.data.dtos.UserDtoGeneric
import com.example.twitterfalso.data.dtos.UserProfileRetrofitDto
import javax.inject.Inject

class UserRetrofitDataSourceImpl @Inject constructor(private val service: UserRetrofitService) : UserRemoteDataSource {

    override suspend fun getUserById(
        id: String,
        currentUserId: String
    ): UserDtoGeneric {
        return service.getUserById("1")
    }

    override suspend fun getUserTweets(id: String): List<TweetDto> {
        return service.getTweetsByUser(id)
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        // to-do
    }

    override suspend fun updateUserProfileImage(userId: String, photoUrl: String) {
        TODO("Not yet implemented")
    }

    override suspend fun followOrUnfollow(currentUserId: String, targetUserId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserProfile(
        userId: String,
        userProfileInfo: UpdateUserDto
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserBackgroundImage(userId: String, photoUrl: String) {
        TODO("Not yet implemented")
    }

    override suspend fun searchUsers(query: String): List<UserDtoGeneric> {
        TODO("Not yet implemented")
    }

}