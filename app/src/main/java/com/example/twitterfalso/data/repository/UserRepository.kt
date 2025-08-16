package com.example.twitterfalso.data.repository

import android.util.Log
import com.example.twitterfalso.data.TweetInfo
import com.example.twitterfalso.data.UserProfileInfo
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.datasource.impl.retrofit.UserRetrofitDataSourceImpl
import com.example.twitterfalso.data.dtos.RegisterUserDto
import com.example.twitterfalso.data.dtos.toTweetInfo
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserFirestoreDataSourceImpl
) {

    suspend fun getUserById(id: String): Result<UserProfileInfo> {
        return try {
            val user = userRemoteDataSource.getUserById(id)
            val userProfileIngo = user.toUserProfileInfo()
            Result.success(userProfileIngo)
        } catch (e: Exception) {
            Log.d("TAG", "getUserById: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun getUserTweets(userId: String): Result<List<TweetInfo>> {
        return try {
            val tweets = userRemoteDataSource.getUserTweets(userId)
            val tweetsInfo = tweets.map { it.toTweetInfo() }
            Result.success(tweetsInfo)
        } catch (e: Exception) {
            Log.d("TAG", "getUserById: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun registerUser(username: String, pais: String?, name: String, bio: String?, userId: String): Result<Unit>{
        return try{
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            val registerUserDto = RegisterUserDto(username, name, pais, bio, fcmToken)
            userRemoteDataSource.registerUser(registerUserDto, userId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("TAG", "getUserById: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateUserProfileImage(userId: String, photoUrl: String): Result<Unit>{
        return try{
            userRemoteDataSource.updateUserProfileImage(userId, photoUrl)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("updateUserProfileImage", "getUserById: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun followOrUnfollow(currentUserId: String, targetUserId: String): Result<Unit>{
        return try{
            userRemoteDataSource.followOrUnfollow(currentUserId, targetUserId)
            return Result.success(Unit)
        } catch (e: Exception) {
            Log.d("updateUserProfileImage", "getUserById: ${e.message}")
            Result.failure(e)
        }

    }

}