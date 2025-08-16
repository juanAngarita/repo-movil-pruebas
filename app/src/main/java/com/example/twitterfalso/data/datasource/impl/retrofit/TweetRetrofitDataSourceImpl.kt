package com.example.twitterfalso.data.datasource.impl.retrofit

import com.example.twitterfalso.data.datasource.TweetRemoteDataSource
import com.example.twitterfalso.data.datasource.services.TweetRetrofitService
import com.example.twitterfalso.data.dtos.CreateTweetDto
import com.example.twitterfalso.data.dtos.TweetDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TweetRetrofitDataSourceImpl @Inject constructor(
    val service: TweetRetrofitService
) : TweetRemoteDataSource {
    override suspend fun getAllTweets(): List<TweetDto> {
        return service.getAllTweets()
    }

    override suspend fun getTweetById(id: String, currentUserId: String): TweetDto {
        return service.getTweetById(id)
    }

    override suspend fun createTweet(tweet: CreateTweetDto) {
        tweet.userId = "1"
        service.createTweet(tweet)
    }

    override suspend fun deleteTweet(id: String): Unit? {
        return service.deleteTweet(id)
    }

    override suspend fun updateTweet(id: String, tweet: CreateTweetDto) {
       service.updateTweet(id, tweet)
    }

    override suspend fun getTweetReplies(id: String): List<TweetDto> {
        return service.getTweetReplies(id)
    }

    override suspend fun sendOrDeleteLike(tweetId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override fun listenAllTweets(): Flow<List<TweetDto>> {
        TODO("Not yet implemented")
    }

}