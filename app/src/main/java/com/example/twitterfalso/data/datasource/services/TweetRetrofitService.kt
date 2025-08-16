package com.example.twitterfalso.data.datasource.services

import com.example.twitterfalso.data.dtos.CreateTweetDto
import com.example.twitterfalso.data.dtos.TweetDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TweetRetrofitService {

    @GET("tweets")
    suspend fun getAllTweets(): List<TweetDto>

    @POST("tweets")
    suspend fun createTweet(@Body tweet: CreateTweetDto): Unit

    @DELETE("tweets/{id}")
    suspend fun  deleteTweet(@Path("id") id: String): Unit

    @PUT("tweets/{id}")
    suspend fun updateTweet(@Path("id") id: String, @Body tweet: CreateTweetDto): Unit

    @GET("tweets/{id}")
    suspend fun getTweetById(@Path("id") id: String): TweetDto

    @GET("tweets/{id}/replies")
    suspend fun getTweetReplies(@Path("id") id: String): List<TweetDto>
}