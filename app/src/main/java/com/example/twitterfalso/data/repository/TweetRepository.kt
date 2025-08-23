package com.example.twitterfalso.data.repository

import android.util.Log
import coil.network.HttpException
import com.example.twitterfalso.data.TweetInfo
import com.example.twitterfalso.data.datasource.AuthRemoteDataSource
import com.example.twitterfalso.data.datasource.impl.firestore.TweetFirestoreDataSourceImpl
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.datasource.impl.retrofit.TweetRetrofitDataSourceImpl
import com.example.twitterfalso.data.dtos.CreateTweetDto
import com.example.twitterfalso.data.dtos.CreateTweetUserDto
import com.example.twitterfalso.data.dtos.toTweetInfo
import com.example.twitterfalso.ui.functions.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TweetRepository @Inject constructor(
    private val tweetRemoteDataSource: TweetFirestoreDataSourceImpl,
    private val userRemoteDataSource: UserFirestoreDataSourceImpl,
    private val authRemoteDataSource: AuthRemoteDataSource
)
{
    suspend fun getTweets(): Result<List<TweetInfo>> {
        return try {
            val tweets = tweetRemoteDataSource.getAllTweets()
            val tweetsInfo = tweets.map { it.toTweetInfo() }
            Result.success(tweetsInfo)

        } catch (e: HttpException){
            Result.failure(e)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createTweet(content: String, userId: String, parentTweetId: String?, tweetId: String?): Result<Unit> {
        return try {

            val user = userRemoteDataSource.getUserById(userId)
            val photoUrl = Utils.getCurrentUserPhoto()

            val createTweetUserDto = CreateTweetUserDto(
                name = user.name,
                username = user.username,
                profileImage = photoUrl
            )


            val createTweetDto = CreateTweetDto(content, userId, parentTweetId, tweetId, createTweetUserDto)
            Log.d("TAG", "createTweet: $tweetId")
            if(tweetId != null) tweetRemoteDataSource.updateTweet(tweetId, createTweetDto)
             else tweetRemoteDataSource.createTweet(createTweetDto)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTweetById(id: String, currentUserId: String = ""): Result<TweetInfo> {
        return try {
            val tweet = tweetRemoteDataSource.getTweetById(id, currentUserId)
            val tweetInfo = tweet.toTweetInfo()
            Result.success(tweetInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTweetsReplies(id:String): Result<List<TweetInfo>> {
        return try {
            val tweets = tweetRemoteDataSource.getTweetReplies(id)
            val tweetsInfo = tweets.map { it.toTweetInfo() }
            Result.success(tweetsInfo)
        } catch (e: HttpException){
            Result.failure(e)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTweet(id: String): Result<Unit?> {
        return try {
            tweetRemoteDataSource.deleteTweet(id)
            Log.d("TAG", "BIEN")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("TAG", "deleteTweet: $e")
            Result.failure(e)
        }
    }

    suspend fun getUserTweets(userId: String, tweetId: String): Result<Unit> {
        return try {
            tweetRemoteDataSource.sendOrDeleteLike(tweetId, userId);
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getTweetsLive(): Flow<List<TweetInfo>> {
        return tweetRemoteDataSource.listenAllTweets()
            .map { list -> list.map { it.toTweetInfo() } }
    }

    suspend fun sendOrDeleteTweetLike(tweetId: String, userId: String): Result<Unit> {
        return try {
            tweetRemoteDataSource.sendOrDeleteLike(tweetId, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}