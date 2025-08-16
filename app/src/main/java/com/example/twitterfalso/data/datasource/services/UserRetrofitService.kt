package com.example.twitterfalso.data.datasource.services

import com.example.twitterfalso.data.dtos.TweetDto
import com.example.twitterfalso.data.dtos.UserProfileRetrofitDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRetrofitService {

    @GET("users/{userId}/tweets")
    suspend fun getTweetsByUser(@Path("userId") userId: String): List<TweetDto>

    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): UserProfileRetrofitDto
}