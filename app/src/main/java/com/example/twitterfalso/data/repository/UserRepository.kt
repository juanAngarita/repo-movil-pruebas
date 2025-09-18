package com.example.twitterfalso.data.repository

import android.util.Log
import com.example.twitterfalso.data.TweetInfo
import com.example.twitterfalso.data.UserProfileInfo
import com.example.twitterfalso.data.datasource.UserRemoteDataSource
import com.example.twitterfalso.data.datasource.impl.firestore.UserFirestoreDataSourceImpl
import com.example.twitterfalso.data.datasource.impl.retrofit.UserRetrofitDataSourceImpl
import com.example.twitterfalso.data.dtos.RegisterUserDto
import com.example.twitterfalso.data.dtos.UpdateUserDto
import com.example.twitterfalso.data.dtos.UserDtoGeneric
import com.example.twitterfalso.data.dtos.toTweetInfo
import com.example.twitterfalso.ui.functions.Utils
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) {

    suspend fun getUserById(id: String, currentUserId: String = ""): Result<UserProfileInfo> {
        return try {
            val user = userRemoteDataSource.getUserById(id, currentUserId)

            if(user == null) return Result.failure(NoSuchElementException("Usuario $id no encontrado"))

            val userProfileIngo = user.toUserProfileInfo()

            Result.success(userProfileIngo)
        } catch (e: Exception) {
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
            val fcmToken = try {
                FirebaseMessaging.getInstance().token.await()
            } catch (e: Exception){
                "Vacio"
            }

            val registerUserDto = RegisterUserDto(username, name, pais, bio, fcmToken)
            userRemoteDataSource.registerUser(registerUserDto, userId)
            Log.d("TAGPRUEBAS", "Pasando el registerUser")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d("TAGPRUEBAS", "Error en registerUser de user repository: ${e.message}")
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

    suspend fun updateUserProfile(userId: String, updateUserDto: UpdateUserDto): Result<Unit>{
        return try{
            userRemoteDataSource.updateUserProfile(userId, updateUserDto)
            return Result.success(Unit)
        } catch (e: Exception) {
            Log.d("updateUserProfileImage", "getUserById: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun updateUserBackgroundImage(userId: String, photoUrl: String): Result<Unit>{
        return try{
            userRemoteDataSource.updateUserBackgroundImage(userId, photoUrl)
            Result.success(Unit)
            } catch (e: Exception) {
            Log.d("updateUserProfileImage", "getUserById: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun searchUsers(query: String): Result<List<UserProfileInfo>>{
        return try{
            val users = userRemoteDataSource.searchUsers(query)
            val usersInfo = users.map { it.toUserProfileInfo() }
            Log.d("TAG", "searchUsers: $usersInfo")
            Result.success(usersInfo)
        } catch (e: Exception) {
            Log.d("TAG", "getUserById: ${e.message}")
            Result.failure(e)
        }
    }

}