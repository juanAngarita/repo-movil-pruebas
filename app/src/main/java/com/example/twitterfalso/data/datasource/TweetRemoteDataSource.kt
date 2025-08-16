package com.example.twitterfalso.data.datasource

import com.example.twitterfalso.data.TweetInfo
import com.example.twitterfalso.data.dtos.CreateTweetDto
import com.example.twitterfalso.data.dtos.TweetDto
import kotlinx.coroutines.flow.Flow

interface TweetRemoteDataSource {
    suspend fun getAllTweets(): List<TweetDto>
    suspend fun getTweetById(id: String, currentUserId: String = ""): TweetDto
    suspend fun createTweet(tweet: CreateTweetDto): Unit
    suspend fun deleteTweet(id: String): Unit?
    suspend fun updateTweet(id: String, tweet: CreateTweetDto): Unit
    suspend fun getTweetReplies(id: String): List<TweetDto>

    //nuevos
    suspend fun sendOrDeleteLike(tweetId: String, userId: String): Unit
    //listenAllTweets
    fun listenAllTweets(): Flow<List<TweetDto>>

}