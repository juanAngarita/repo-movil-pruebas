package com.example.twitterfalso.data.datasource.impl.firestore

import com.example.twitterfalso.data.datasource.UserRemoteDataSource
import com.example.twitterfalso.data.dtos.RegisterUserDto
import com.example.twitterfalso.data.dtos.TweetDto
import com.example.twitterfalso.data.dtos.UserDtoGeneric
import com.example.twitterfalso.data.dtos.UserProfileFirestoreDto
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFirestoreDataSourceImpl @Inject constructor(private val db: FirebaseFirestore) : UserRemoteDataSource {

    override suspend fun getUserById(id: String): UserProfileFirestoreDto {
        val docRef = db.collection("users").document(id)
        val respuesta = docRef.get().await()
        return respuesta.toObject(UserProfileFirestoreDto::class.java) ?: throw Exception("No se pudo obtener el usuario")
    }

    override suspend fun getUserTweets(id: String): List<TweetDto> {
        val snapshot = db.collection("tweets").whereEqualTo("userId", id).get().await()
        return snapshot.documents.map { doc ->
            val tweet = doc.toObject(TweetDto::class.java)
            tweet?.copy(id = doc.id) ?: throw Exception("Tweet not found")
        }
    }

    override suspend fun registerUser(registerUserDto: RegisterUserDto, userId: String) {
        val docRef = db.collection("users").document(userId) //se crea un documento
        docRef.set(registerUserDto).await() // insertando el documento
    }

    override suspend fun updateUserProfileImage(userId: String, photoUrl: String) {
        val docRef = db.collection("users").document(userId)
        docRef.update("profileImage", photoUrl).await()
    }

    override suspend fun followOrUnfollow(currentUserId: String, targetUserId: String) {
        val currentUserRef = db.collection("users").document(currentUserId)
        val targetUserRef = db.collection("users").document(targetUserId)

        val followingRef = currentUserRef.collection("following").document(targetUserId)
        val followerRef = targetUserRef.collection("followers").document(currentUserId)

        db.runTransaction { transaction ->
            val followingDoc = transaction.get(followingRef)

            if (followingDoc.exists()) {
                // ❌ Dejar de seguir
                transaction.delete(followingRef)
                transaction.delete(followerRef)
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(-1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(-1))
            } else {
                // ✅ Seguir
                transaction.set(followingRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.set(followerRef, mapOf("timestamp" to FieldValue.serverTimestamp()))
                transaction.update(currentUserRef, "followingCount", FieldValue.increment(1))
                transaction.update(targetUserRef, "followersCount", FieldValue.increment(1))
            }
        }
    }
}